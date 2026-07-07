package su.nightexpress.excellentclaims.rules.evaluation.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventController;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;

@NullMarked
public abstract class AbstractEventController implements EventController {

    protected final EvaluatorEngine engine;

    protected AbstractEventController(EvaluatorEngine engine) {
        this.engine = engine;
    }
}