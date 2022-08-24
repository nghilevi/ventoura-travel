package com.Mindelo.VentouraServer.Main;

import org.jivesoftware.smack.XMPPConnection;
import org.springframework.beans.factory.annotation.Autowired;

import com.Mindelo.VentouraServer.Constant.IMConstant;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.Service.IMService;

public class IMManager {

	@Autowired
	private static IIMService imService;

	private static XMPPConnection connection;

	public IMManager() {
		imService = new IMService();
	}

	public static void init() {
		try {
			connection = imService.getConnection(IMConstant.URL_IM_SERVER,
					IMConstant.IM_SERVER_PORT, IMConstant.SERVICE);

			connection.login(IMConstant.VENTOURA_SERVER_IM_ACCOUNT,
					IMConstant.VENTOURA_SERVER_IM_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static XMPPConnection getXMPPConnection() {
		if (!connection.isConnected()) {
			init();
		}
		return connection;
	}

}
