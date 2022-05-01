package sets;

import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {
  private boolean complement;
  private FiniteSet points;

  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    this(false, FiniteSet.of(vals));
  }

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    this.complement = complement;
    this.points = points;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;
    return this.complement == other.complement
            && this.points.equals(other.points);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    if (complement) {
      return Float.POSITIVE_INFINITY;
    } else {
      return points.size();
    }
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {
    String ans = "";
    if (complement && points.size() == 0) {
      ans = "R";
    } else if (points.size() == 0) {
      ans = "{}";
    } else {
      if (complement) {
        ans += "R \\ ";
      }
      ans += "{";
      List<Float> set = points.getPoints();
      ans += set.get(0);
      for (int i = 1; i < set.size(); i++) {
        ans += ", " + set.get(i);
      }
      ans += "}";
    }
    return ans;
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    if (complement) {
      return new SimpleSet(false, this.points);
    } else {
      return new SimpleSet(true, this.points);
    }
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.union(other.points));
    } else if (this.complement && other.complement) {
      return new SimpleSet(true, this.points.intersection(other.points));
    } else if (this.complement && !other.complement) {
      return new SimpleSet(true, this.points.difference(other.points));
    } else {
      return new SimpleSet(true, other.points.difference(this.points));
    }
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.intersection(other.points));
    } else if (this.complement && other.complement) {
      return new SimpleSet(true, this.points.union(other.points));
    } else if (this.complement && !other.complement) {
      return new SimpleSet(false, other.points.difference(this.points));
    } else {
      return new SimpleSet(false, this.points.difference(other.points));
    }
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    if (!this.complement && !other.complement) {
      return new SimpleSet(false, this.points.difference(other.points));
    } else if (this.complement && other.complement) {
      return new SimpleSet(false, other.points.difference(this.points));
    } else if (this.complement && !other.complement) {
      return new SimpleSet(true, this.points.union(other.points));
    } else {
      return new SimpleSet(false, this.points.intersection(other.points));
    }
  }

}
