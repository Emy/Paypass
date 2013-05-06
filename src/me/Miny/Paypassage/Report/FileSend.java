package me.Miny.Paypassage.Report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.Miny.Paypassage.Paypassage;
import me.Miny.Paypassage.logger.LoggerUtility;

public class FileSend {

    private Paypassage plugin;

    public FileSend(Paypassage plugin) {
        this.plugin = plugin;
    }

    public void sendDebugFile(final String errorid) throws IOException {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://report.ibhh.de/logs/send.php?plugin=" + FileSend.this.plugin.getName() + "&ID=" + errorid);
                    Date now = new Date();
                    String path = FileSend.this.plugin.getDataFolder().toString() + File.separator + "debugfiles" + File.separator;
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd 'at' HH");
                    File file = new File(path + "debug-" + ft.format(now) + ".txt");
                    if (file.exists()) {
                        // Construct data
                        String data = URLEncoder.encode("file", "UTF-8") + "=";
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        try {
                            String line = null;
                            while ((line = reader.readLine()) != null) {
                                data += URLEncoder.encode(line, "UTF-8") + "\n";
                            }
                        } catch (Exception e) {
                        }
                        // Send data
                        URLConnection conn = url.openConnection();
                        conn.setDoOutput(true);
                        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                        wr.write(data);
                        wr.flush();

                        // Get the response
                        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = rd.readLine()) != null) {
                            if (line.contains("Successfully")) {
                                System.out.println(line);
                            }
                        }
                        wr.close();
                        rd.close();
                    }
                } catch (Exception e) {
                    plugin.getLoggerUtility().log("cannot send debugfile because the linking of some resources failed.", LoggerUtility.Level.ERROR);
                    plugin.getLoggerUtility().log("May you used /reload and therefore it doesnt work.", LoggerUtility.Level.ERROR);
                }
            }
        });
    }
}