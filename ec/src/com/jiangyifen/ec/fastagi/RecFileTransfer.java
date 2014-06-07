package com.jiangyifen.ec.fastagi;

import java.io.IOException;

import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

import com.jiangyifen.ec.util.Config;
import com.jiangyifen.ec.util.Wav2Mp3;

//import com.jiangyifen.ec.util.RecFileFtpUploader;

public class RecFileTransfer extends BaseAgiScript {
	private final Log logger = LogFactory.getLog(getClass());

	public void service(AgiRequest request, AgiChannel channel)
			throws AgiException {

		String recLocalMemPath = null;
		String recLocalDiskPath = null;
		String recFileName = null;
		String recMp3 = null;

		recLocalMemPath = Config.props.getProperty("rec_local_mem_path");
		recLocalDiskPath = Config.props.getProperty("rec_local_disk_path");
		recMp3 = Config.props.getProperty("rec_mp3");

		recFileName = this.getVariable("recFileName");
		if (recFileName != null) {
			// 先将录制在内存里的录音文件移动到本地磁盘
			// 然后将磁盘上的文件试着上传到录音FTP服务器
			// 为什么不直接上传到FTP？因为考虑到如果网络断开导致无法上传，录音文件将很快塞满内存，或者在重启时丢失
			String dir = recFileName.substring(0, 8) + "/";
			try {
				Thread.sleep(2000);
				String cmd;

				cmd = "mkdir -p " + recLocalDiskPath + dir;
				// logger.info(cmd);
				Runtime.getRuntime().exec(cmd);

				if (recMp3.toLowerCase().equals("true")) {
					cmd = "lame " + recLocalMemPath + recFileName + " "
							+ recLocalDiskPath + dir + recFileName;
					Wav2Mp3.addTask(cmd);

					cmd = "rm " + recLocalMemPath + recFileName;
					Wav2Mp3.addTask(cmd);

				} else {
					cmd = "mv " + recLocalMemPath + recFileName + ".WAV" + " "
							+ recLocalDiskPath + dir + recFileName + ".wav"
							+ " -f";
					// logger.info(cmd);
					Runtime.getRuntime().exec(cmd);

					cmd = "mv " + recLocalMemPath + recFileName + ".wav" + " "
							+ recLocalDiskPath + dir + recFileName + ".wav"
							+ " -f";
					// logger.info(cmd);
					Runtime.getRuntime().exec(cmd);
				}

				// RecFileFtpUploader rffl = new RecFileFtpUploader();
				// if (rffl.upload(recLocalDiskPath + recFileName, recFileName))
				// {
				// try {
				// String cc = "rm " + recLocalDiskPath + recFileName
				// + " -f";
				// logger.info(cc);
				// Runtime.getRuntime().exec(cc);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				// }

			} catch (IOException e) {
				logger.error(e.getMessage(), e);

			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}
}