package loaders;

import helpers.Storage;
import helpers.Room;

public class RoomLoader extends BaseLoader {
		
	public RoomLoader()
	{
		filename = "files/rooms.txt";
		storage = Storage.getInstance();
	}
	
	protected void writeStorage()
	{
		iterator = json.iterator();
        Long code,capacity;
        String room;
		 while (iterator.hasNext()) {
			 j = iterator.next();
			 
			 code = (Long)j.get("code");
			 capacity = (Long)j.get("capacity");
			 room = (String) j.get("room");
			 storage.addRoom(new Room(room, code.intValue(), capacity.intValue()));
        }
	}
}
