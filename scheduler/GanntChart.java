package scheduler;

import java.util.ArrayList;

public class GanntChart {
	private long systemStartTime; // wall time when the Gannt chart starts.  Is used
								// to display all timings as relative to this time
	private ArrayList<GanntRecord> events = new ArrayList<GanntRecord>();

	public GanntChart(){
		start();

	}
	
	public void start(){
		systemStartTime = System.currentTimeMillis(); // set os start time
	}
	
	public void recordEvent(long startTime, long endTime, String eventDescriptor) {
		events.add(new GanntRecord(startTime, endTime, eventDescriptor));
	}
	
	public void end() {
		long endTime = System.currentTimeMillis();
	    events.add(new GanntRecord(endTime, endTime, "FINISHED"));
	}
	
	public void print() {
		System.out.println("GANNT CHART:");
		System.out.println("BurstStart		BurstEnd	JOB");
		for(int i=0; i<events.size(); i++){
			GanntRecord record = events.get(i);
			System.out.printf("%d			%d		%s",record.startTime-systemStartTime,record.endTime-systemStartTime,record.eventDescriptor);
			System.out.println();
		}
	}
	

	private class GanntRecord {
		long startTime;
		long endTime;
		String eventDescriptor;
		
		GanntRecord(long start, long end, String descrip){
			startTime = start;
			endTime = end;
			eventDescriptor = descrip;
		}
	}

}
