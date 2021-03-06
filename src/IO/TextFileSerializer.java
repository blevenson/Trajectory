package IO;

import Generator.Path;
import Generator.Trajectory;
import Generator.Trajectory.Segment;

/**
 * Serializes a Path to a simple space and CR separated text file.
 * 
 * @author Jared341
 */
public class TextFileSerializer {

  /**
   * Format:
   *   PathName
   *   NumSegments
   *   LeftSegment1
   *   ...
   *   LeftSegmentN
   *   RightSegment1
   *   ...
   *   RightSegmentN
   * 
   * Each segment is in the format:
   *   pos vel acc jerk heading dt x y
   * 
   * @param path The path to serialize.
   * @return A string representation.
   */
  public String serialize(Path path) {
    String content = path.getName() + "\n";
    path.goLeft();
    content += path.getLeftWheelTrajectory().getNumSegments() + "\n";
    content += serializeTrajectory(path.getLeftWheelTrajectory());
    content += serializeTrajectory(path.getRightWheelTrajectory());
    return content;
  }
  
  private String serializeTrajectory(Trajectory trajectory) {
    String content = "";
    for (int i = 0; i < trajectory.getNumSegments(); ++i) {
      Segment segment = trajectory.getSegment(i);
      content += String.format(
              "%.3f %.3f %.3f %.3f %.3f %.3f %.3f %.3f\n", 
              segment.pos, segment.vel, segment.acc, segment.jerk,
              segment.heading, segment.dt, segment.x, segment.y);
    }
    return content;
  }
  
}
