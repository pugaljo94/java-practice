package javatest;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;



class Result {

    /*
     * Complete the 'droppedRequests' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts INTEGER_ARRAY requestTime as parameter.
     */

    public static int droppedRequests(List<Integer> requestTime) {
    // Write your code here
    final int MAX_PER_SECOND = 3;
    final int MAX_TEN_SECONDS = 20;
    final int MAX_PER_MINUTE = 60;
    int[] requestTimeArray = new int[requestTime.size()];
    for(int i = 0;i < requestTimeArray.length;i++)
        requestTimeArray[i] = requestTime.get(i);
    if (requestTime == null || requestTimeArray.length == 0) {
            return 0;
        }
        int drop = 0;
        Map<Integer, Integer> map = new HashMap<>();
        int lastReqTime = 0;
        for (int i : requestTime) {
            map.put(i, map.getOrDefault(i, 0) + 1);
            lastReqTime = Math.max(lastReqTime, i);
        }
        int[] nums = new int[lastReqTime + 1];
        for (int i = 1; i < nums.length; ++i) {
            int numReqThisSecond = map.getOrDefault(i, 0);
            nums[i] = nums[i - 1] + numReqThisSecond;
            if (numReqThisSecond == 0) {
                continue;
            }
            int toDrop = 0;
            if (numReqThisSecond > MAX_PER_SECOND) {
                toDrop = Math.max(toDrop, numReqThisSecond - MAX_PER_SECOND);
            }

            int timeTenSecondsAgo = Math.max(i - 10, 0);
            int numReqPastTenSecond = nums[i] - nums[timeTenSecondsAgo];
            if (numReqPastTenSecond > MAX_TEN_SECONDS) {
                int numReqExceeded = Math.min(numReqThisSecond, numReqPastTenSecond - MAX_TEN_SECONDS);
                toDrop = Math.max(toDrop, numReqExceeded);
            }

            int timeOneMinuteAgo = Math.max(i - 60, 0);
            int numReqPastMinute = nums[i] - nums[timeOneMinuteAgo];
            if (numReqPastMinute > MAX_PER_MINUTE) {
                int numReqExceeded = Math.min(numReqThisSecond, numReqPastMinute - MAX_PER_MINUTE);
                toDrop = Math.max(toDrop, numReqExceeded);
            }
            drop += toDrop;
        }
        return drop;
    }

}

public class ThrottlingGateway {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int requestTimeCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> requestTime = IntStream.range(0, requestTimeCount).mapToObj(i -> {
            try {
                return bufferedReader.readLine().replaceAll("\\s+$", "");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        })
            .map(String::trim)
            .map(Integer::parseInt)
            .collect(toList());

        int result = Result.droppedRequests(requestTime);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
