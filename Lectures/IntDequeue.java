/**
 * A sequence that supports efficient insert/remove at both ends.
 */
public class IntDequeue {

  // RI: vals != null and 0 <= start < vals.length and 0 <= len <= vals.length
  // AF(this) =
  //   vals[start..start+len-1]            if start+len <= vals.length
  //   vals[start..] + vals[0..len-(vals.length-start)-1]    otherwise
  private int[] vals;
  private int start, len;

  /** @effects creates an empty sequence */
  public IntDequeue() {
    vals = new int[3];
    start = len = 0;
  }

  /** @returns the length of this */
  public int getLength() { return len; }

  /** @requires 0 <= index < length
    * @returns this[index] */
  public int get(int index) {
    if (start + index < vals.length) {
      return vals[start + index];
    } else {
      return vals[start + index - vals.length];
    }
  }

  /** @requires 0 <= index < length
    * @modifies this
    * @effects this_post = this_pre[..index-1] + [val] + this_pre[index+1..] */
  public void set(int index, int val) {
    if (start + index < vals.length) {
      vals[start + index] = val;
    } else {
      vals[start + index - vals.length] = val;
    }
  }

  /** @modifies this
    * @effects inserts val at the beginning of the sequence */
  public void unshift(int val) {
    ensureMoreSpace();
    start = (start > 0) ? start - 1 : vals.length - 1;
    len += 1;
    vals[start] = val;
    checkRep();
  }

  /** @requires length of this > 0
    * @modifies this
    * @effects removes the value at the beginning of the sequence
    * @returns the value that was removed  */
  public int shift() {
    int val = get(0);
    start = (start + 1 == vals.length) ? 0 : start + 1;
    len -= 1;
    checkRep();
    return val;
  }

  /** @modifies this
    * @effects inserts val at the end of the sequence */
  public void push(int val) {
    ensureMoreSpace();
    len += 1;
    set(len - 1, val);
    checkRep();
  }

  /** @requires length of this > 0
    * @modifies this
    * @effects removes the value at the end of the sequence
    * @returns the value that was removed  */
  public int pop() {
    int val = get(len-1);
    len -= 1;
    checkRep();
    return val;
  }

  /** @modifies this
    * @effects this is unchanged and len < vals.length */
  private void ensureMoreSpace() {
    if (len == vals.length) {
      int[] newVals = new int[Math.max(1, 2*vals.length)];
      if (start + len <= vals.length) {
        System.arraycopy(vals, start, newVals, 0, len);
      } else {
        System.arraycopy(vals, start, newVals, 0, vals.length - start);
        System.arraycopy(vals, 0, newVals, vals.length - start,
            len - (vals.length - start));
      }
      start = 0;
      vals = newVals;
    }
  }

  /** Checks that RI holds. */
  private void checkRep() {
    assert vals != null;
    assert 0 <= start && start < vals.length;
    assert 0 <= len && len <= vals.length;
  }
}
