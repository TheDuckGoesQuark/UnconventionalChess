Look into AKKA as an alternative to JADE since it supports spring integration and might be easier to set up a little web UI for :) 

Agent architecture groups:
1. Logic based (symbolic)
	environment is symbolically represented and manipulated. 

2. Reactive
	Stimulus-response mechanism

3. BDI
	Belief, desire, intention
	Beliefs represent information an agent has about its environment which may be incomplete or incorrect.
	Desires represent tasks allocated to the agent (goals, objectives)
	Intentions are desires that the agent is committed to achieving.
	Plans specify some course of action that may be followed by an agent to achieve its intentions

4. Layered
	Both reactive and deliberative behaviour.
	Horizontal: layers are connected directly to sensory input and action output (requires decision as to what layer to take action from)
	Vertical: Control flows from initial sensor layer to final layer that generates action output.

## FIPA Message Structure

Envelope
- Payload
  |-Message
	|-Content


# Glossary

Agent Platform (AP):
The physical infrastructure in which agents are deployed.
Consists of the machines, operating systems, FIPA agent management components, the agents themselves, and any additional software.

Agent:
A computational process that inhabits an AP, and typically offers one or more computational services that can be publishes as a service description.

Directory Facilitator (DF):
An optional component of an AP that provides a yellow pages service to other agents. It maintains an accurate, complete, and timely list of agents and must provide the most current information about agents in its directory.

Agent Management System (AMS)
Mandatory component of an AP, handles the creation and deletion of agents, and their migration between hosts.

Message Transport Service (MTS)
Transports FIPA-ACL messages between agents.


