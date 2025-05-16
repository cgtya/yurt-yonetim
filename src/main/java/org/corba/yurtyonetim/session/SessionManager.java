package org.corba.yurtyonetim.session;

import org.corba.yurtyonetim.users.Manager;

public class SessionManager {
    //giriş yapan kullanıcıyı tutan değişken
    private static Manager loggedInManager;

    public static Manager getLoggedInManager() {
        return loggedInManager;
    }
    public static void setLoggedInManager(Manager loggedInManager) {
        SessionManager.loggedInManager = loggedInManager;
    }
}
