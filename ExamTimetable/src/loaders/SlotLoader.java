package loaders;

import helpers.*;

public class SlotLoader extends BaseLoader {
		
	public SlotLoader()
	{
		filename = "files/slots.txt";
		storage = Storage.getInstance();
	}
	
	protected void writeStorage()
	{
		iterator = json.iterator();
         Long time,day,month;
		 while (iterator.hasNext()) {
			 j = iterator.next();
			 
			 day = (Long)j.get("day");
			 time = (Long)j.get("time");
			 month = (Long)j.get("month");
			 
             storage.addSlot(new Slot(time.intValue(), day.intValue(), month.intValue()));
         }
	}
}
