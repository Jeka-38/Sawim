package protocol.xmpp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Jid {
    private static final String[] transports = {"mrim.", "icq.", "picq.", "pyicq.", "sicq.", "j2j."};
    private static final String[] icqTransports = {"icq.", "picq.", "pyicq."};
    public static final Pattern pattern = Pattern.compile("(([^ @/<>'\\\"]+)@)([^ @/<>'\\\"]+)\\.([a-zA-Z\\.]{2,6})(?:/([^ <>'\\\"]*))?");

    public static String realJidToSawimJid(String realJid) {
        if (isIrcConference(realJid)) {
            int index = realJid.indexOf('!');
            if (-1 != index) {
                return realJid.substring(index + 1)
                        + '/' + realJid.substring(0, index);
            }
            index = realJid.indexOf('%');
            if ((-1 != index) && (-1 != realJid.indexOf('/', realJid.indexOf('@')))) {
                return realJid.substring(index + 1);
            }
        }
        String resource = getResource(realJid, null);
        String jid = getBareJid(realJid);
        return (null == resource) ? jid : (jid + '/' + resource);
    }

    public static String SawimJidToRealJid(String SawimJid) {
        if (isIrcConference(SawimJid) && (-1 != SawimJid.indexOf('/', SawimJid.indexOf('@')))) {
            String bareJid = getBareJid(SawimJid);
            if (-1 != bareJid.indexOf('%')) {
                bareJid = bareJid.substring(bareJid.indexOf('%') + 1);
            }
            return getResource(SawimJid, "") + '!' + bareJid;
        }
        return SawimJid;
    }

    public static String getDomain(String jid) {
        jid = getBareJid(jid);
        return jid.substring(jid.indexOf('@') + 1);
    }

    public static String getResource(String fullJid, String defResource) {
        int resourceStart = fullJid.indexOf('/') + 1;
        if (0 < resourceStart) {
            return fullJid.substring(resourceStart);
        }
        return defResource;
    }

    public static boolean isJID(String jid) {
        Matcher matcher = pattern.matcher(jid);
        return matcher.matches();
    }

    public static boolean isConference(String server, String jid) {
        int index = jid.indexOf('@');
        if (-1 < index) {
            if (jid.startsWith(server, index + 1)) {
                return true;
            }
            int index1 = jid.lastIndexOf('%', index);
            if (-1 < index1) {
                return jid.startsWith(server, index1 + 1);
            }
        }
        return false;
    }

    public static String makeReadableJid(String jid) {
        jid = jid.replace("@conference.jabber.ru", "@c.j.ru");
        return jid.replace("@conference.", "@c.");
    }

    public static boolean isGate(String jid) {
        return (-1 == jid.indexOf('@')) && (0 < jid.length());
    }

    public static boolean isPyIcqGate(String jid) {
        if (!isGate(jid)) {
            return false;
        }
        for (int i = 0; i < icqTransports.length; ++i) {
            if (jid.startsWith(icqTransports[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMrim(String jid) {
        return (-1 != jid.indexOf("@mrim."));
    }

    public static boolean isIrcConference(String jid) {
        return (-1 != jid.indexOf("@irc."));
    }

    public static boolean isKnownGate(String jid) {
        if (Jid.isGate(jid)) {
            for (int i = 0; i < transports.length; ++i) {
                if (jid.startsWith(transports[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getBareJid(String fullJid) {
        int resourceStart = fullJid.indexOf('/');
        if (-1 != resourceStart) {
            return fullJid.substring(0, resourceStart).toLowerCase();
        }
        return fullJid.toLowerCase();
    }

    public static String getNick(String jid) {
        return jid.substring(0, jid.indexOf('@'));
    }

    public static String getNormalJid(String jid) {
        String bare = getBareJid(jid);
        if (-1 == jid.indexOf('/')) {
            return bare;
        }
        return bare + '/' + getResource(jid, null);
    }
}


