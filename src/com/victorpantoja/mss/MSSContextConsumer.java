package com.victorpantoja.mss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;
import br.rio.puc.inf.lac.mobilis.cms.ContextConsumer;
import br.rio.puc.inf.lac.mobilis.cms.ContextInformationObject;

/**
 * A Context Consumer.
 * 
 * @author victor.pantoja
 */
public class MSSContextConsumer extends ContextConsumer {

	protected static final String TAG = "mss-consumer";
	private SharedPreferences.Editor editor;

	/**
	 * Constructor.
	 * 
	 * @param context the application context
	 * @param cli
	 */
	public MSSContextConsumer(Context context, SharedPreferences.Editor editor) {
		super(context);
		this.editor = editor;
	}

	/**
	 * Sends a new log to the mains screen.
	 * 
	 * @param log the new log
	 */
	private void sendLog(String log) {
		Log.d(TAG,log);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void newContextInformation(ContextInformationObject info) {
		Log.d("consumer", "nova informacao de contexto, class=" + info.getInformationClass() + ", device=" + info.getDevice());
		if (info.getInformationClass().equals("battery") && info.containsContextInformation("isOk")) {
			sendLog("n�vel da bateria: " + info.getContextInformation("level"));
			sendLog("temperatura da bateria: " + info.getContextInformation("temperature"));
			if (info.getContextInformation("isOk").equals("true")) {
				sendLog("a bateria esta ok :) - entao vamos procurar a nossa localizacao");
				try {
					this.addContextInformationInterest("this.location.latitude");
					sendLog("interesse localizacao adicionado");
				}
				catch (RemoteException e) {
					Log.e("consumer", "erro ao adicionar consumer location", e);
				}
			}
			else {
				sendLog("a bateria nao esta ok :(");
			}
		}
		else if (info.getInformationClass().equals("location")) {
			sendLog("estamos em: lat=" + info.getContextInformation("latitude") + ", lng=" + info.getContextInformation("longitude"));
			
			this.editor.putString("location", info.getContextInformation("latitude")+","+info.getContextInformation("longitude"));
			this.editor.commit();
		}
		else if (info.getInformationClass().equals("time")) {
			sendLog("currentTime=" + info.getContextInformation("currentTime") + ", dayTurn=" + info.getContextInformation("dayTurn"));
			try {
				sendLog("configurar propriedade");
				int i = getCmsInterface().setProviderConfiguration("this.time.hour", "refreshInterval", "5");
				if (i == 0) {
					sendLog("setado");
				}
				else {
					sendLog("erro: " + i);
				}
			}
			catch (RemoteException e) {
				sendLog("nao pode :(");
				Log.e("consumer", "erro ao setar configuracao", e);
			}
		}
	}
}
