package loader;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public class neo4jConnector {

	public neo4jConnector() {

		connectToDatabaseByBolt();
	}

	private Driver driver;

	private void connectToDatabaseByBolt() {

		String uri = "bolt://localhost:7687";
		String username = "neo4j";
		String password = "bisbii";
		driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
	}

	public void close() throws Exception
	{
		driver.close();
	}

	public Driver getDriver() {
		return driver;
	}


	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}