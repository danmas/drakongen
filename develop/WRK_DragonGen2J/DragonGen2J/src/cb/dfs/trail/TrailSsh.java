package cb.dfs.trail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

public class TrailSsh {
	
	public static void do_trail(TrailBase trail, Session session, String command) {
		PrintStream printOut = null;
		OutputStream output = null;
		try {
			session.connect();

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			InputStream in = channel.getInputStream();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			printOut = new PrintStream(os);

			// ((ChannelExec)channel).setErrStream(System.err);
			((ChannelExec) channel).setErrStream(printOut);

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					trail.addRetOutStr(new String(tmp, 0, i));
				}
				if (channel.isClosed()) {
					break;
				}
			}
			int ret = channel.getExitStatus();

			channel.disconnect();
			session.disconnect();

			printOut.flush();
			
			if (ret == 0) {
				trail.addRetOutStr(os.toString("UTF8"));
				trail.setStatusSuccess();
			} else {
				String str = "При выполнении ssh сценария произошла ошибка. " + os.toString("UTF8");
				trail.addRetErrStr(str);
				trail.setStatusError();
			}
		} catch (Exception e) {
			// System.err.println(e.getMessage());
			trail.addRetErrStr("При выполнении ssh сценария произошла ошибка: " + e.getMessage());
			trail.setStatusError();
		} finally {
			if (printOut != null)
				printOut.flush();
			if (output != null) {
				try {
					output.flush();
					output.close();
				} catch (Exception e) {
					// System.err.println(e.getMessage());
					//trail.addRetErrStr("ERROR: " + e.getMessage());
					//trail.setStatusError();
				}
			}
		}
	}

}