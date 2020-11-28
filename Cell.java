package kis.sspd.jade.game;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

@SuppressWarnings("serial")
public class Cell extends Agent {
	private Integer x;
	private Integer y;
	private Integer X;
	private Integer Y;

	private Boolean currentState;
	private Boolean nextState;

	private List<Integer> deadToAlive;
	private List<Integer> remainAlive;

	protected void setup(){
		Object[] args = getArguments();
		this.x = Integer.parseInt(args[0].toString());
		this.y = Integer.parseInt(args[1].toString());
		this.X = Integer.parseInt(args[2].toString());
		this.Y = Integer.parseInt(args[3].toString());
		this.currentState = Boolean.parseBoolean(args[4].toString());

		Rule rule = new ConwayRule();
		deadToAlive = rule.getDeadToAlive();
		remainAlive = rule.getRemainAlive();

		addBehaviour(new Ask());
		addBehaviour(new Listen());
	}
	
	
	private class Ask extends CyclicBehaviour {
		public void action() {
			ACLMessage askBoard = new ACLMessage(ACLMessage.REQUEST);
			askBoard.setContent("numberOfNeighbours " + x + " " + y);
			askBoard.addReceiver(new AID("board", false));
			send(askBoard);
		}
	}

	private class Listen extends CyclicBehaviour {
		public void action() {
			ACLMessage message = myAgent.blockingReceive();
			if (message != null && message.getPerformative() == ACLMessage.INFORM) {
				String content = message.getContent();
				String[] args = content.split(" ");
				Integer numberOfAliveNeighbours = Integer.parseInt(args[0]);

				calculateNewState(numberOfAliveNeighbours);

				ACLMessage reply = message.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(currentState.toString() + " " +  x + " " + y);
				send(reply);
			}
		}
	}

	private void calculateNewState(Integer numberOfAliveNeighbours){
		if(this.currentState && deadToAlive.stream().anyMatch(number -> number.equals(numberOfAliveNeighbours))){
			//this.nextState = Boolean.TRUE;
			this.currentState = Boolean.TRUE;
			return;
		}
		if(this.currentState  && remainAlive.stream().anyMatch(number -> number.equals(numberOfAliveNeighbours))){
			//this.nextState = Boolean.TRUE;
			this.currentState = Boolean.TRUE;
			return;
		}
		//this.nextState = Boolean.FALSE;
		this.currentState = Boolean.FALSE;
	}

	protected void takeDown(){
		System.out.println("Cell " + getLocalName() + " has been destroyed.");
	}	
}
