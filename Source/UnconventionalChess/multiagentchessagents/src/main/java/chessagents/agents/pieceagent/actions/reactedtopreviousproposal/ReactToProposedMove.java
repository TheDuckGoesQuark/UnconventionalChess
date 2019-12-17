package chessagents.agents.pieceagent.actions.reactedtopreviousproposal;

import chessagents.agents.pieceagent.ActionResponse;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.behaviours.play.PieceTransition;
import chessagents.chess.GameState;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.util.RandomUtil;
import simplenlg.features.Feature;
import simplenlg.features.Tense;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;
import static chessagents.agents.pieceagent.nlg.NLGUtil.REALISER;

public class ReactToProposedMove extends PieceAction {

    final GameState gameStateBeforeMove;
    final PieceAction proposedMoveAction;

    /**
     * @param actor               piece performing the action
     * @param gameStateBeforeMove the game state before the proposed move would be carried out
     * @param proposedMoveAction  the move being proposed
     */
    public ReactToProposedMove(ChessPiece actor, GameState gameStateBeforeMove, PieceAction proposedMoveAction) {
        super(PieceTransition.REACTED_TO_PREVIOUS_PROPOSAL, "React to proposed move", actor, true);
        this.gameStateBeforeMove = gameStateBeforeMove;
        this.proposedMoveAction = proposedMoveAction;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }

    @Override
    public Optional<String> verbalise(PieceContext context) {
        var responses = context.getPersonality().getResponseToActions(getActor(), Collections.singleton(proposedMoveAction), gameStateBeforeMove).get(proposedMoveAction);

        // separate positive and negative responses
        var positiveResponses = responses.stream().filter(ActionResponse::isApproveAction).collect(Collectors.toSet());
        var negativeResponses = responses.stream().filter(Predicate.not(ActionResponse::isApproveAction)).collect(Collectors.toSet());

        // choose one response from the majority
        var rand = new RandomUtil<ActionResponse>();
        final ActionResponse chosenResponse;

        if (positiveResponses.size() == 0 && negativeResponses.size() == 0) logger.warning("BOTH AT ZERO");
        if (positiveResponses.size() != 0 && positiveResponses.size() >= negativeResponses.size()) {
            chosenResponse = rand.chooseRandom(positiveResponses);
        } else {
            chosenResponse = rand.chooseRandom(negativeResponses);
        }

        // verbalise it
        var sentence = NLG_FACTORY.createClause();
        var i = NLG_FACTORY.createNounPhrase("I");
        sentence.setSubject(i);
        var opinion = NLG_FACTORY.createVerbPhrase("like");
        sentence.setVerb(opinion);
        if (!chosenResponse.isApproveAction()) sentence.setFeature(Feature.NEGATED, true);
        var subject = NLG_FACTORY.createNounPhrase("move");
        subject.setDeterminer("that");
        sentence.setObject(subject);
        sentence.setFeature(Feature.TENSE, Tense.PRESENT);

        var reasoning = chosenResponse.getReasoning();
        if (reasoning != null) {
            reasoning.setFeature(Feature.COMPLEMENTISER, "because");
            reasoning.setFeature(Feature.TENSE, Tense.FUTURE);
            sentence.addComplement(reasoning);
        }


        return Optional.ofNullable(REALISER.realiseSentence(sentence));
    }
}
