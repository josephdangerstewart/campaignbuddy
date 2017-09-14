package com.campaignbuddy.main;

import org.json.JSONObject;

import javax.swing.filechooser.FileSystemView;
import java.io.*;

/**
 * Created by josephstewart on 8/4/17.
 */
public class Prefs {

    private JSONObject prefs;

    private File home;
    private String folderName = "My First Campaign";
    private File prefsFile;

    private String sideBarApp = "";

    public Prefs() {
        this.prefs = new JSONObject();
        File file = FileSystemView.getFileSystemView().getHomeDirectory();
        home = new File(file,"CampaignBuddy");
        if (!home.exists()) {
            home.mkdirs();
        }
        prefsFile = null;
    }

    public boolean setCampaign(String campaign) {
        this.folderName = campaign;

        System.out.println("Setting campaign " + campaign);

        File folder = new File(home, folderName);
        prefsFile = new File(folder,  "/settings.dat");

        try {
            if (!prefsFile.exists()) {
                if (!folder.exists())
                    folder.mkdirs();
                prefsFile.createNewFile();
                PrintWriter writer = new PrintWriter(new FileWriter(prefsFile));
                writer.println("{}");
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new FileReader(prefsFile));
            String line = reader.readLine();
            reader.close();

            System.out.println(line);

            this.prefs = new JSONObject(line);

            if (prefs.has("_sideBarApp")) {
                this.sideBarApp = prefs.getString("_sideBarApp");
            } else {
                prefs.put("_sideBarApp","");
                save();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String[] getCampaigns() {
        File[] files = home.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        String[] fileNames = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getName();
        }

        return fileNames;
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void removeCampaign(String campaign) {
        File file = new File(home,campaign);

        System.out.println(file.getAbsolutePath());

        if (file.exists() && file.isDirectory()) {
            System.out.println("IS DIR");
            deleteFolder(file);
            if (campaign.equals(folderName)) {
                prefs = new JSONObject();
                prefsFile = null;
            }
        }
    }

    public void save() {
        if (prefsFile != null) {
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(prefsFile));
                writer.println(prefs.toString());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSideBarApp(String sideBarApp) {
        this.sideBarApp = sideBarApp;
        prefs.put("_sideBarApp",sideBarApp);
        save();
    }

    public String getSideBarApp() {
        return sideBarApp;
    }

    public String getCampaignName() {
        return folderName;
    }

    public JSONObject getAppPrefs(String appName) {
        System.out.println("GETTING APP PREFS FOR '" + appName + "'");
        if (prefs.has(appName)) {
            System.out.println("HAS PREFS");
            return prefs.getJSONObject(appName);
        } else {
            return null;
        }
    }

    public void saveAppPrefs(String appName,JSONObject prefs) {
        this.prefs.put(appName,prefs);
        save();
    }

}
