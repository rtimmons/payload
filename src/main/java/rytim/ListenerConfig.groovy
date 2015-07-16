package rytim
/**
 * @author rtimmons
 * @date 6/20/15
 */
class ListenerConfig {
    List<String> listensOn = []
    Map msgBindIn;
    Map inputSchema;

    String submitsOn;
    Map msgBindOut;
    Map outputSchema;

    String logCategorySuffix;
    Map metricKeys;
}
