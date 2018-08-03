import java.util.LinkedList;
import java.util.List;

/**
 * Created by Laptop-6 on 2017/12/14.
 */
public class TestSubList {
    public static void main(String[] args) {
        List<Long> seriesPath = new LinkedList<>();

        seriesPath.add(1L);
        seriesPath.add(2L);
        seriesPath.add(3L);
        seriesPath.add(4L);
        seriesPath.add(5L);

        long startPoint = -1;
        long endPoint = -5;

        LinkedList<Long> emptyPathStart = null;
        LinkedList<Long> scanPodPath = null;
        LinkedList<Long> emptyPathEnd = null;
        if(startPoint == 0 && endPoint == 0){
            scanPodPath = new LinkedList<>(seriesPath);
        }else if (startPoint == 0 && endPoint != 0){
            scanPodPath = new LinkedList<>(seriesPath.subList(0, seriesPath.lastIndexOf(endPoint) + 1));
            emptyPathEnd = new LinkedList<>(seriesPath.subList(seriesPath.lastIndexOf(endPoint), seriesPath.size()));
        }else if(startPoint != 0 && endPoint == 0){
            emptyPathStart = new LinkedList<>(seriesPath.subList(0, seriesPath.indexOf(startPoint) + 1));
            scanPodPath = new LinkedList<>(seriesPath.subList(seriesPath.indexOf(startPoint), seriesPath.size()));
        }else if(startPoint != 0 && endPoint != 0){
            emptyPathStart = new LinkedList<>(seriesPath.subList(0, seriesPath.indexOf(startPoint) + 1));
            scanPodPath = new LinkedList<>(seriesPath.subList(seriesPath.indexOf(startPoint), seriesPath.lastIndexOf(endPoint) + 1));
            emptyPathEnd = new LinkedList<>(seriesPath.subList(seriesPath.lastIndexOf(endPoint), seriesPath.size()));
        }


        System.out.println("emptyPathStart - - > > "+emptyPathStart);
        System.out.println("scanPodPath - - > > "+scanPodPath);
        System.out.println("emptyPathEnd - - > > "+emptyPathEnd);


    }
}
