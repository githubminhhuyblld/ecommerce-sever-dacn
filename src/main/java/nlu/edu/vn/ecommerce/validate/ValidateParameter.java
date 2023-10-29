package nlu.edu.vn.ecommerce.validate;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;


public class ValidateParameter {
    /**
     * Validate the existence and non-null value of expected parameters in a request.
     *
     * @param request The HttpServletRequest object.
     * @param expectedParams The expected parameter names.
     * @return true if all expected parameters exist and have non-null values, false otherwise.
     */
    public static boolean validateParams(HttpServletRequest request, String... expectedParams) {
        Map<String, String[]> paramMap = request.getParameterMap();

        // Check the existence of all expected parameters
        if (!paramMap.keySet().containsAll(Arrays.asList(expectedParams))) {
            return false;
        }

        // Check for null values for all expected parameters
        for (String param : expectedParams) {
            String[] values = paramMap.get(param);
            if (values == null || values.length == 0 || "null".equals(values[0])) {
                return false;
            }
        }

        return true;
    }

    //validate params
    public static boolean validateRequestParams(HttpServletRequest request, String... expectedParams) {
        Set<String> paramNames = request.getParameterMap().keySet();
        return paramNames.containsAll(Arrays.asList(expectedParams)) && paramNames.size() == expectedParams.length;
    }
}
