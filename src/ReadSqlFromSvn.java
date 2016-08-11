

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author figueiredo
 */
public class ReadSqlFromSvn {

    private String svnUrl;
    private String username;
    private String password;
    private String ext;
    private final String regex = "\"(\\(|\\w).*?({param})\"";
    private final List<MatchResult> matches = new ArrayList<>();
    private final List<String> scripts = new ArrayList<>();

    public ReadSqlFromSvn(final String svnUrl) {
        this.svnUrl = svnUrl;
        getSvnUrl();
    }

    public ReadSqlFromSvn(final String svnUrl, final String username, final String password) {
        this(svnUrl);
        this.username = username;
        this.password = password;
    }

    public void readPage() throws MalformedURLException, IOException {
        try (final BufferedReader in = getUrlReader("")) {
            String inputLine;
            final StringBuilder sb = new StringBuilder();
            getExtension();
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            getScripts(sb.toString());
        }

    }

    public BufferedReader getUrlReader(final String scriptFile) throws MalformedURLException, IOException {
        final URLConnection uc = new URL(svnUrl + scriptFile).openConnection();
        String userpass = getUsernameAndPassword();
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        uc.setRequestProperty("Authorization", basicAuth);
        return new BufferedReader(new InputStreamReader(uc.getInputStream()));
    }

    public List<String> getScripts() {
        return scripts;
    }

    public String getSearchString() {
        return getString("O que deseja procurar: ");
    }

    public void addMatch(final MatchResult matchResult) {
        matches.add(matchResult);
    }

    public void getSearchResult() {
        if (matches.isEmpty()) {
            System.out.println("Não foram encontrados valores para a pesquisa feita.");
        } else {
            System.out.println("Texto encontrado no(s) arquivo(s): \n");
            for (MatchResult matchResult : matches) {
                System.out.println(matchResult);
            }
        }
    }

    private void getScripts(final String page) {
        final Pattern pattern = Pattern.compile(getRegex(), Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(page);
        while (matcher.find()) {
            scripts.add(matcher.group().replace("\"", ""));
        }
    }
    
    private String getRegex() {
        return regex.replace("{param}", ext);
    }
    
    private String getExtension() {
        ext = getString("Qual será a extensão do arquivo? (SQL) ");
        if(ext == null || ext.equals("")) {
            ext = "sql";
        }
        return ext;
    }

    private void getSvnUrl() {
        if (svnUrl == null) {
            svnUrl = getString("Digite o nome da pasta dos scripts (caminho do svn completo): ");
        }
    }

    private String getUsernameAndPassword() {
        if (username == null) {
            username = getString("Usuário: ");
        }
        if (password == null) {
            password = getString("Senha: ");
        }
        return username + ":" + password;
    }

    private String getString(final String text) {
        System.out.print(text);
        return new Scanner(System.in).nextLine();
    }

}
