package chessagents.agents.gatewayagent;

import chessagents.agents.ChessMessageBuilder;
import chessagents.ontology.ChessOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.wrapper.gateway.GatewayAgent;

/**
 * Custom gateway agent implementation in order to support the ontology when serialising messages
 * Can't extend ChessAgent since Java only supports extending one class, and we require the gateway agent implementation
 */
public class ChessGatewayAgent extends GatewayAgent implements ChessMessageBuilder {

    private Codec codec = new SLCodec();
    private Ontology ontology = ChessOntology.getInstance();

    @Override
    protected void setup() {
        super.setup();
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
    }
}
