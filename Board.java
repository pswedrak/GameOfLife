package kis.sspd.jade.game;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class Board extends Agent {
	private AgentController[][] cells;
	private Integer Xsize;
	private Integer Ysize;
	private Integer[][] state;
	private Boolean[][] received;
	private NeighbourhoodCreator neighbourhoodCreator = new MooreNeighbourhoodCreator();

	private JFrame jFrame;
	private String[] columnNames;
	private JScrollPane previousComponent;

	FileOutputStream outputStream;

	protected void setup() {
		Object[] args = getArguments();
		this.Xsize = Integer.parseInt(args[0].toString());
		this.Ysize = Integer.parseInt(args[1].toString());
		try {
			outputStream = new FileOutputStream("report.txt");
			outputStream.write(String.valueOf(this.Xsize).getBytes());
			outputStream.write("\n".getBytes());
			outputStream.write(String.valueOf(this.Ysize).getBytes());
			outputStream.write("\n".getBytes());
			outputStream.write(String.valueOf(new Date().getTime()).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		jFrame = new JFrame();
		columnNames = new String[Ysize];
		for(int i=0; i<columnNames.length; i++){
			columnNames[i] = " ";
		}
		jFrame.setTitle("Game of life");

		initializeBoard();
		addBehaviour(new Listen());
		System.out.println("Board " + getLocalName() + " is ready.");
	}

	private void initializeBoard(){
		cells = new AgentController[Xsize][Ysize];
		state = new Integer[Xsize][Ysize];
		received = new Boolean[Xsize][Ysize];
		clearReceived();
		Random random = new Random();

		for (int x = 0; x < cells.length; ++x)
			for (int y = 0; y < cells[x].length; ++y) {
				Double prob = random.nextDouble();
				Boolean isAlive = false;

				if(prob < 0.3){
					isAlive = true;
				}

				Object[] args = new Object[5];
				args[0] = x;
				args[1] = y;
				args[2] = cells.length;
				args[3] = cells[x].length;
				args[4] = isAlive;

				try {
					AgentController cell = getContainerController().createNewAgent(
							"cell" + x + "_" + y, "kis.sspd.jade.game.Cell", args);
					cell.start();
					cells[x][y] = cell;
					state[x][y] = isAlive ? 1 : 0;
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}

		JTable jTable = new JTable(convertToString(state), columnNames);
		JScrollPane sp = new JScrollPane(jTable);
		jFrame.add(sp);
		jFrame.setSize(25*Xsize, 25*Ysize);
		jFrame.setVisible(true);
		previousComponent = sp;
	}

	private class Listen extends CyclicBehaviour {
		public void action() {
			ACLMessage message = myAgent.receive();

			if (message != null && message.getPerformative() == ACLMessage.REQUEST) {
				String content = message.getContent();
				String[] args = content.split(" ");
				Integer sender_x = Integer.parseInt(args[1]);
				Integer sender_y = Integer.parseInt(args[2]);
				List<Point> neighbours = neighbourhoodCreator.createNeighbourhood(sender_x, sender_y, Xsize, Ysize);
				Integer aliveNeighbours = 0;

				for(Point neighbour: neighbours){
					if(state[neighbour.x][neighbour.y] == 1){
						aliveNeighbours++;
					}
				}

				ACLMessage reply = message.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(aliveNeighbours.toString());
				send(reply);
			}

			if (message != null && message.getPerformative() == ACLMessage.INFORM) {
				String content = message.getContent();
				String[] args = content.split(" ");
				Boolean newState = Boolean.parseBoolean(args[0]);
				Integer sender_x = Integer.parseInt(args[1]);
				Integer sender_y = Integer.parseInt(args[2]);

				state[sender_x][sender_y] = newState ? 1 : 0;
				received[sender_x][sender_y] = true;

				if(receivedAll()){
					try {
						outputStream.write("\n".getBytes());
						outputStream.write(String.valueOf(new Date().getTime()).getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}

					clearReceived();
					jFrame.remove(previousComponent);

					JTable jTable = new JTable(convertToString(state), columnNames);
					JScrollPane sp = new JScrollPane(jTable);
					jFrame.add(sp);
					jFrame.setSize(25*Xsize, 25*Ysize);
					jFrame.setVisible(true);
					previousComponent = sp;
				}

			}
		}
	}

	protected void takeDown() {
		try {
			outputStream.close();
			for (int x = 0; x < cells.length; ++x){
				for (int y = 0; y < cells[x].length; ++y) {
					cells[x][y].kill();
				}
			}
		} catch (StaleProxyException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("The game is over.");
	}

	private void clearReceived(){
		for (int x = 0; x < cells.length; ++x){
			for (int y = 0; y < cells[x].length; ++y) {
				received[x][y] = false;
			}
		}
	}

	private Boolean receivedAll(){
		for (int x = 0; x < cells.length; ++x){
			for (int y = 0; y < cells[x].length; ++y) {
				if(!received[x][y]){
					return false;
				}
			}
		}
		return true;
	}

	private String[][] convertToString(Integer[][] cells){
		String[][] stringData = new String[Xsize][Ysize];
		for (int x = 0; x < cells.length; ++x){
			for (int y = 0; y < cells[x].length; ++y) {
				stringData[x][y] = state[x][y].toString();
			}
		}

		return stringData;
	}
}