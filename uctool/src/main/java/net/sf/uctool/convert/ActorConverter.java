package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.ExtendsActor;
import net.sf.uctool.xsd.UseCase;

public class ActorConverter {

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	public ActorConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public ActorOut convert(Actor actor) {
		ActorOut o = new ActorOut();
		String code = actor.getCode();
		o.setCode(code);
		o.setName(escape(actor.getName()));

		for (DescriptionType descriptionType : actor.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "actor", code);
			}
			o.getDescriptions().add(sb.toString().trim());
		}

		Set<Actor> extendedActors = new LinkedHashSet<Actor>();
		for (ExtendsActor extendsActor : actor.getExtendsActor()) {
			String extendsCode = extendsActor.getCode();
			Actor extended = executionContext.getActors().get(extendsCode);
			if (null == extended) {
				throw new ValidationException("Missing actor with code ["
						+ extendsCode + "] referenced from actor with code ["
						+ code + "].");
			}
			o.getExtendsActors().add(
					new Reference("actor", extendsCode, extended.getName()));
			extendedActors.add(extended);
		}

		for (Actor other : executionContext.getActors().values()) {
			for (ExtendsActor extendsActor : other.getExtendsActor()) {
				if (extendsActor.getCode().equals(code)) {
					o.getExtendedByActors().add(
							new Reference("actor", other.getCode(), other
									.getName()));
				}
			}
		}

		List<UseCase> goals = getGoals(actor);
		for (UseCase useCase : goals) {
			o.getGoals().add(
					new Reference("uc", useCase.getCode(), useCase.getCode()
							+ " - " + useCase.getGoal()));
		}

		Set<Actor> inheritedActors = new LinkedHashSet<Actor>();
		for (Actor extended : extendedActors) {
			getTransitiveExtendedActors(extended, inheritedActors);
		}
		for (Actor inherited : inheritedActors) {
			o.getInheritedActors().add(
					new Reference("actor", inherited.getCode(), inherited
							.getName()));
		}

		Set<Actor> transitiveExtendedActors = new LinkedHashSet<Actor>();
		transitiveExtendedActors.addAll(extendedActors);
		transitiveExtendedActors.addAll(inheritedActors);
		Set<UseCase> inheritedGoals = new LinkedHashSet<UseCase>();
		for (Actor transitiveExtended : transitiveExtendedActors) {
			List<UseCase> actorGoals = getGoals(transitiveExtended);
			inheritedGoals.addAll(actorGoals);
		}
		for (UseCase useCase : inheritedGoals) {
			o.getInheritedGoals().add(
					new Reference("uc", useCase.getCode(), useCase.getCode()
							+ " - " + useCase.getGoal()));
		}
		return o;
	}

	private List<UseCase> getGoals(Actor actor) {
		List<UseCase> goals = new ArrayList<UseCase>();
		for (UseCase useCase : executionContext.getUseCases().values()) {
			if (executionContext.getUcGroups().get(useCase.getCode())
					.getPrimaryActor().equals(actor.getCode())) {
				goals.add(useCase);
			}
		}
		return goals;
	}

	private void getTransitiveExtendedActors(Actor actor, Set<Actor> list) {
		for (ExtendsActor extendsActor : actor.getExtendsActor()) {
			Actor extended = executionContext.getActors().get(
					extendsActor.getCode());
			list.add(extended);
			getTransitiveExtendedActors(extended, list);
		}
	}

}
