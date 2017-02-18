package pokerGame.Controllers;

import java.net.URL;
import java.rmi.Naming;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pokerGame.Entities.User;
import pokerGame.Entities.Room;
import pokerGame.Entities.TableRoom;
import pokerGame.Scenes.GameScenes;
import server.rmi.ILobby;

public class RoomsPaneController extends BaseController {
	
	@FXML
	private Button addNewRoomButton;
	
	@FXML
	private Button joinRoomButton;
	
	@FXML
	private TableView<TableRoom> roomListTableView;
	
	/*Columns*/
	/*Columns*/
	private ObservableList<TableRoom> roomList;
	
	private ListProperty<TableRoom> listProperty;
	
	private LobbyController lobbyController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		roomList = FXCollections.observableArrayList();
        listProperty = new SimpleListProperty<TableRoom>();
        listProperty.set(roomList);
        roomListTableView.itemsProperty().bind(listProperty);
        
		roomListTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("roomName"));
		roomListTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		roomListTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("userCount"));
	}

	@FXML
	private void onAddRoomButtonAction(ActionEvent e){
		lobbyController.switchRoomsPane(mainController.getScene(GameScenes.NEW_ROOM_PANE));
	}
	
	@FXML
	private void onJoinRoomButtonAction(ActionEvent e){
		try{
			ILobby lobby = (ILobby)Naming.lookup("rmi://"+mainController.getSettings().getServerLocation()+"/lobby");
			Room newRoom = lobby.joinRoom(mainController.getUser(), roomListTableView.getSelectionModel().getSelectedItem().getRoomName());
			if(newRoom !=  null){
				mainController.setRoom(newRoom);
				((RoomController)mainController.getController(GameScenes.ROOM)).bindRoom(newRoom);
				((RoomController)mainController.getController(GameScenes.ROOM)).setStartGameButton(mainController.getUser());
				lobbyController.switchRoomsPane(mainController.getScene(GameScenes.ROOM));
			}else
				System.out.println("No room selected");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	
	@FXML
	private void onMuseClickedAction(MouseEvent e){
		if(roomListTableView.getSelectionModel().getSelectedItem() != null){
			joinRoomButton.setDisable(false);
		}else{
			joinRoomButton.setDisable(true);
		}
	}
	
	public void setLobbyController(LobbyController controller){
		this.lobbyController = controller;
	}
	
	public void populateRoomList(List<Room> roomList){
		roomList.clear();
		for(Room r : roomList){
			TableRoom room = new TableRoom();
			room.setRoomName(r.getRoomName());
			for(User p : r.getUserList()){
				if(r.getRoomOwner().getUsername().equals(p.getUsername())){
					room.setOwnerName(p.getUsername());
					break;
				}
			
			}
			room.setUserCount(r.getUserList().size());
			this.roomList.add(room);
		}
	}
	
	public void updateTableRoom(Room room){
		for(TableRoom r : roomList){
			if(r.getRoomName().equals(room.getRoomName())){
				r.setUserCount(room.getUserList().size());
			}
		}
	}
	
	public void addTableRoom(Room room){
		TableRoom r = new TableRoom();
		r.setRoomName(room.getRoomName());
		for(User p : room.getUserList()){
			if(room.getRoomOwner().getUsername().equals(p.getUsername())){
				r.setOwnerName(p.getUsername());
				break;
			}
		}
		r.setUserCount(room.getUserList().size());
		roomList.add(r);
	}
	
	public void removeTableRoom(Room room){
		for(TableRoom r : roomList){
			if(r.getRoomName().equals(room.getRoomName())){
				roomList.remove(r);
				break;
			}
		}
	}
	
}
