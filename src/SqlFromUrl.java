
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author figueiredo
 */
public class SqlFromUrl {

    private static String svnUrl;
    private static String username;
    private static String password;
    private static String searchString;

    public static void main(String[] args) throws IOException {
        final ReadSqlFromSvn sqlFromSvn = readArgs(args);
        sqlFromSvn.readPage();
        searchString = sqlFromSvn.getSearchString();
        for (String script : sqlFromSvn.getScripts()) {
            try (final BufferedReader in = sqlFromSvn.getUrlReader(script)) {
                runFile(sqlFromSvn, in, script);
            } catch (final IOException ex) {
                System.out.println("Erro no script: " + script);
                ex.printStackTrace();
            }
        }
        sqlFromSvn.getSearchResult();
    }

    private static void runFile(final ReadSqlFromSvn sqlFromSvn, final BufferedReader in, final String script)
            throws IOException {
        final MatchResult matchResult = new MatchResult(script);
        int lineNumber = 1;
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.toLowerCase().contains(searchString.toLowerCase())) {
                matchResult.addLineMatch(lineNumber);
            }
            lineNumber++;
        }
        if (matchResult.matched()) {
            sqlFromSvn.addMatch(matchResult);
        }
    }

    private static ReadSqlFromSvn readArgs(final String[] args) {
        if (args.length == 0) {
            return new ReadSqlFromSvn(null);
        }
        if ("--help".equals(args[0]) || "-h".equals(args[0])) {
            printHelp();
            System.exit(0);
            return null;
        } else {
            if (args[0].contains("--") || args[0].length() == 2) {
                addValue(args, 0);
                addValue(args, 2);
                addValue(args, 4);
            } else {
                final String[] subArgs = args[0].substring(1).split("");
                addValue(subArgs, args);
            }
            return new ReadSqlFromSvn(svnUrl, username, password);
        }
    }

    private static void addValue(final String[] args, final int index) {
        if (args[index] != null) {
            switch (args[index]) {
                case "--user":
                    username = args[index + 1];
                case "-u":
                    username = args[index + 1];
                    break;
                case "--svn":
                    svnUrl = args[index + 1];
                case "-s":
                    svnUrl = args[index + 1];
                    break;
                case "--pass":
                    password = args[index + 1];
                case "-p":
                    password = args[index + 1];
                    break;
            }
        }
    }

    private static void addValue(final String[] subArgs, final String[] args) {
        int index = 1;
        for (String subArg : subArgs) {
            switch (subArg) {
                case "u":
                    username = args[index];
                    break;
                case "s":
                    svnUrl = args[index];
                    break;
                case "p":
                    password = args[index];
                    break;
                default:
                    index--;
                    break;
            }
            index++;
        }
    }

    private static void printHelp() {
        System.out.println("Help: ");
        System.out.println("\t--help ou -h\t\t\tHelp do sistema.");
        System.out.println("\t--user ou -u\t\t\tUsuário de conexão com o svn.");
        System.out.println("\t--pass ou -p\t\t\tSenha de conexão com o svn.");
        System.out.println("\t--svn ou -s\t\t\tURL do svn.");
        System.out.println("");
        System.out.println("Exemplo:");
        System.out.println("java -jar FindInSvn.jar -sup http://url/do/svn usuario senha");
        System.out.println("");
        System.out.println("PS: Os parâmetros de URL, Usuário e Senha são obrigatórios. Caso não informados no comando, serão solicitados na execução.");
    }

}
