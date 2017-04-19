package session_mng;

import javax.websocket.Session;

public interface GamePartyInterface {

	
	public boolean joinPlayer(Session hostSession);
	public void sendUpdatePlayerList();
	
	
	public void sendLetterNList();
	public void sendVoteList();
	public void sendResultList();

	public void reset();
	
}
