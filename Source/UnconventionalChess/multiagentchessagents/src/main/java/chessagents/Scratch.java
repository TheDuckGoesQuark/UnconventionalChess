package chessagents;

import chessagents.agents.gatewayagent.messages.ChatMessage;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.Position;
import chessagents.util.RandomUtil;
import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import simplenlg.features.*;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.Arrays;
import java.util.Optional;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;

class Scratch {
    public static void main(String[] args) {
    }
}