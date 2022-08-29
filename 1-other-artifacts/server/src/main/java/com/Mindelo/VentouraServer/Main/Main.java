package com.Mindelo.VentouraServer.Main;



public class Main
{
	public static void main(String... anArgs) throws Exception
    {
        new Main().start();
    }
	
	public static void stop(){
		;
	}
	
	private WebServer server;
    
    public Main()
    {
        server = new WebServer(80);        
    }
    
    public void start() throws Exception
    {
        server.start();        
        server.join();
    }
}
