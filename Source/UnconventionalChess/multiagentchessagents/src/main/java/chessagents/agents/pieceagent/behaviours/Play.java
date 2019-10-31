package chessagents.agents.pieceagent.behaviours;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.chess.BoardWrapper;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.concepts.Piece;
import com.github.bhlangonijr.chesslib.Board;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static chessagents.agents.pieceagent.PieceAgent.*;

public class Play extends FSMBehaviour {

    private final PieceContext context;

    public Play(PieceAgent pieceAgent, PieceContext context) {
        super(pieceAgent);
        this.context= context;
    }
}
