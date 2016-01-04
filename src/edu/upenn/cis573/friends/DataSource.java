package edu.upenn.cis573.friends;
import java.util.List;

/*
 * This is the interface that is implemented by the other DataSources.
 */

public interface DataSource {
	
	public List<String> get(String arg);

}
