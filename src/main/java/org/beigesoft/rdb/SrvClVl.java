/*
BSD 2-Clause License

Copyright (c) 2019, Beigesoft™
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.beigesoft.rdb;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.mdl.ColVals;
import org.beigesoft.prp.ISetng;

/**
 * <p>Column values shared utility.</p>
 *
 * @author Yury Demidenko
 */
public class SrvClVl {

  /**
   * <p>ORM setting service.</p>
   **/
  private ISetng setng;

  /**
   * <p>Makes string representation.</p>
   * @param pCls entity class
   * @param pClVl column values
   * @return string representation
   * @throws Exception an Exception
   **/
  public final String str(final Class<?> pCls,
    final ColVals pClVl) throws Exception {
    StringBuffer sb = new StringBuffer("IDs column names: ");
    boolean isFirst = true;
    for (String idNm : this.setng.lazIdFldNms(pCls)) {
      if (isFirst) {
        isFirst = false;
      } else {
        sb.append(",");
      }
      sb.append(idNm);
    }
    if (pClVl.getOldVer() != null) {
      sb.append("\nOld version=" + pClVl.getOldVer());
    }
    if (pClVl.getExprs() != null) {
      sb.append("\nColumns with expression: ");
      isFirst = true;
      for (String clNm : pClVl.getExprs()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(clNm);
      }
    }
    sb.append("\nvalues: ");
    if (pClVl.getInts() != null) {
      for (Map.Entry<String, Integer> ent : pClVl.getInts().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pClVl.getLongs() != null) {
      for (Map.Entry<String, Long> ent : pClVl.getLongs().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pClVl.getFloats() != null) {
      for (Map.Entry<String, Float> ent : pClVl.getFloats().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pClVl.getDoubles() != null) {
      for (Map.Entry<String, Double> ent : pClVl.getDoubles().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pClVl.getStrs() != null) {
      for (Map.Entry<String, String> ent : pClVl.getStrs().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          sb.append(",");
        }
        sb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    return sb.toString();
  }

  /**
   * <p>Put into column Long value.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pClVl, final String pNm,
    final Long pVal) {
    if (pClVl.getLongs() == null) {
      pClVl.setLongs(new HashMap<String, Long>());
    }
    pClVl.getLongs().put(pNm, pVal);
  }

  /**
   * <p>Put into column String value.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pClVl, final String pNm,
    final String pVal) {
    if (pClVl.getStrs() == null) {
      pClVl.setStrs(new HashMap<String, String>());
    }
    pClVl.getStrs().put(pNm, pVal);
  }

  /**
   * <p>Put into column Integer val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pClVl, final String pNm,
    final Integer pVal) {
    if (pClVl.getInts() == null) {
      pClVl.setInts(new HashMap<String, Integer>());
    }
    pClVl.getInts().put(pNm, pVal);
  }

  /**
   * <p>Put into column Float val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pClVl, final String pNm,
    final Float pVal) {
    if (pClVl.getFloats() == null) {
      pClVl.setFloats(new HashMap<String, Float>());
    }
    pClVl.getFloats().put(pNm, pVal);
  }

  /**
   * <p>Put into column Double val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pClVl, final String pNm,
    final Double pVal) {
    if (pClVl.getDoubles() == null) {
      pClVl.setDoubles(new HashMap<String, Double>());
    }
    pClVl.getDoubles().put(pNm, pVal);
  }

  /**
   * <p>Get column with Integer val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Integer column val
   * @throws ExcCode if field not found
   **/
  public final Integer getInt(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getInts() != null && pClVl.getInts().keySet().contains(pNm)) {
      return pClVl.getInts().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Get column with Long value or ID.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Long column val
   * @throws ExcCode if field not found
   **/
  public final Long getLong(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getLongs() != null && pClVl.getLongs().keySet().contains(pNm)) {
      return pClVl.getLongs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Get column with String val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return String column val
   * @throws ExcCode if field not found
   **/
  public final String getString(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getStrs() != null && pClVl.getStrs().keySet().contains(pNm)) {
      return pClVl.getStrs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Get ID column with String or Long value to check if null.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Object column val
   * @throws ExcCode if field not found
   **/
  public final Object getIdVl(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getLongs() != null && pClVl.getLongs().keySet().contains(pNm)) {
      return pClVl.getLongs().get(pNm);
    }
    if (pClVl.getStrs() != null && pClVl.getStrs().keySet().contains(pNm)) {
      return pClVl.getStrs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Get column with Float val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Float column val
   * @throws ExcCode if field not found
   **/
  public final Float getFloat(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getFloats() != null && pClVl.getFloats().keySet().contains(pNm)) {
      return pClVl.getFloats().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Get column with Double val.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Double column val
   * @throws ExcCode if field not found
   **/
  public final Double getDouble(final ColVals pClVl,
    final String pNm) throws ExcCode {
  if (pClVl.getDoubles() != null && pClVl.getDoubles().keySet().contains(pNm)) {
      return pClVl.getDoubles().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Evaluate column val for SQL statement INSERT or UPDATE.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return String column val
   * @throws ExcCode if column not found
   **/
  public final String evSqlVl(final ColVals pClVl,
    final String pNm) throws ExcCode {
    Object val = evObjVl(pClVl, pNm);
    if (val == null) {
      return "null";
    } else {
      return val.toString();
    }
  }

  /**
   * <p>Evaluate column string val for
   * SQL statement INSERT or UPDATE.
   * For String '[val]' or NULL</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return Object column val
   * @throws ExcCode if column not found
   **/
  public final Object evObjVl(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getInts() != null && pClVl.getInts().keySet().contains(pNm)) {
      return pClVl.getInts().get(pNm);
    }
    if (pClVl.getLongs() != null && pClVl.getLongs().keySet().contains(pNm)) {
      return pClVl.getLongs().get(pNm);
    }
    if (pClVl.getStrs() != null && pClVl.getStrs().keySet().contains(pNm)) {
      String val = (String) pClVl.getStrs().get(pNm);
      if (val == null) {
        return null;
      } else {
        if (pClVl.getExprs() != null && pClVl.getExprs().contains(pNm)) {
          return val;
        } else {
          return "'" + val + "'";
        }
      }
    }
    if (pClVl.getFloats() != null && pClVl.getFloats().keySet().contains(pNm)) {
      return pClVl.getFloats().get(pNm);
    }
    if (pClVl.getDoubles() != null
      && pClVl.getDoubles().keySet().contains(pNm)) {
      return pClVl.getDoubles().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field - " + pNm);
  }

  /**
   * <p>Check whether column is contained here.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @return if contains
    **/
  public final boolean contains(final ColVals pClVl, final String pNm) {
    if (pClVl.getInts() != null && pClVl.getInts().keySet().contains(pNm)) {
      return true;
    }
    if (pClVl.getLongs() != null && pClVl.getLongs().keySet().contains(pNm)) {
      return true;
    }
    if (pClVl.getStrs() != null && pClVl.getStrs().keySet().contains(pNm)) {
      return true;
    }
    if (pClVl.getFloats() != null && pClVl.getFloats().keySet().contains(pNm)) {
      return true;
    }
  if (pClVl.getDoubles() != null && pClVl.getDoubles().keySet().contains(pNm)) {
      return true;
    }
    return false;
  }

  /**
   * <p>Remove column.</p>
   * @param pClVl type-safe map column name - column value
   * @param pNm column name
   * @throws ExcCode if column not found
   **/
  public final void remove(final ColVals pClVl,
    final String pNm) throws ExcCode {
    if (pClVl.getInts() != null && pClVl.getInts().keySet().contains(pNm)) {
      pClVl.getInts().remove(pNm);
      return;
    }
    if (pClVl.getLongs() != null && pClVl.getLongs().keySet().contains(pNm)) {
      pClVl.getLongs().remove(pNm);
      return;
    }
    if (pClVl.getStrs() != null && pClVl.getStrs().keySet().contains(pNm)) {
      pClVl.getStrs().remove(pNm);
      return;
    }
    if (pClVl.getFloats() != null && pClVl.getFloats().keySet().contains(pNm)) {
      pClVl.getFloats().remove(pNm);
      return;
    }
    if (pClVl.getDoubles() != null
      && pClVl.getDoubles().keySet().contains(pNm)) {
      pClVl.getDoubles().remove(pNm);
      return;
    }
    throw new ExcCode(ExcCode.WRPR, "There is no column " + pNm);
  }

  /**
   * <p>Evaluate SQL insert statement. It's assumed that ID maybe either Long
   * or String type, i.e. Integer is not.</p>
   * @param pCls entity class
   * @param pClVl type-safe map column name - column value
   * @return insert query
   * @throws Exception - an exception
   **/
  public final String evInsert(final Class<?> pCls,
    final ColVals pClVl) throws Exception {
    StringBuffer res = new StringBuffer("insert into "
      + pCls.getSimpleName().toUpperCase() + " (");
    StringBuffer vls = new StringBuffer(" values (");
    boolean isFst = true;
    List<String> idNms = this.setng.lazIdFldNms(pCls);
    //ID-able Long and String:
    if (pClVl.getLongs() != null) {
      for (Map.Entry<String, Long> ent : pClVl.getLongs().entrySet()) {
        if (ent.getValue() != null || !idNms.contains(ent.getKey())) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
            vls.append(", ");
          }
          res.append(ent.getKey().toUpperCase());
          vls.append(evSqlVl(pClVl, ent.getKey()));
        }
      }
    }
    if (pClVl.getStrs() != null) {
      for (Map.Entry<String, String> ent : pClVl.getStrs().entrySet()) {
        if (ent.getValue() != null || !idNms.contains(ent.getKey())) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
            vls.append(", ");
          }
          res.append(ent.getKey().toUpperCase());
          vls.append(evSqlVl(pClVl, ent.getKey()));
        }
      }
    }
    if (pClVl.getInts() != null) {
      for (Map.Entry<String, Integer> ent : pClVl.getInts().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pClVl, ent.getKey()));
      }
    }
    if (pClVl.getFloats() != null) {
      for (Map.Entry<String, Float> ent : pClVl.getFloats().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pClVl, ent.getKey()));
      }
    }
    if (pClVl.getDoubles() != null) {
      for (Map.Entry<String, Double> ent : pClVl.getDoubles().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pClVl, ent.getKey()));
      }
    }
    res.append(") " + vls + ");");
    return res.toString();
  }

  /**
   * <p>Evaluate where conditions for SQL update standard statement
   * with optimistic locking if need.</p>
   * @param pCls entity class
   * @param pClVl type-safe map column name - column value
   * @return where conditions e.g. "IID=1 and VER=2"
   * @throws Exception - an exception
   **/
  public final String evWheUpd(final Class<?> pCls,
    final ColVals pClVl) throws Exception {
    StringBuffer sb = new StringBuffer("");
    boolean isFst = true;
    for (String idNm : this.setng.lazIdFldNms(pCls)) {
      if (isFst) {
        isFst = false;
      } else {
        sb.append(" and ");
      }
      sb.append(idNm.toUpperCase() + "=" + evSqlVl(pClVl, idNm));
    }
    if (pClVl.getOldVer() != null) {
      sb.append(" and VER=" + pClVl.getOldVer());
    }
    return sb.toString();
  }

  /**
   * <p>Evaluate SQL update standard statement
   * with optimistic locking if need.</p>
   * @param pCls entity class
   * @param pClVl type-safe map column name - column value
   * @return update statement
   * @throws Exception - an exception
   **/
  public final String evUpdate(final Class<?> pCls,
    final ColVals pClVl) throws Exception {
    String cnd = evWheUpd(pCls, pClVl);
    return evUpdateCnd(pCls, pClVl, cnd);
  }

  /**
   * <p>Evaluate SQL update statement with given conditions (nullable).
   * It's assumed that ID maybe either Long or String type!</p>
   * @param pCls entity class
   * @param pClVl type-safe map column name - column value
   * @param pCnd where conditions e.g. "IID=2 and VER=2" or NULL
   * @return update statement
   * @throws Exception - an exception
   **/
  public final String evUpdateCnd(final Class<?> pCls,
    final ColVals pClVl, final String pCnd) throws Exception {
    StringBuffer res = new StringBuffer("update "
      + pCls.getSimpleName().toUpperCase() + " set ");
    List<String> idNms = this.setng.lazIdFldNms(pCls);
    boolean isFst = true;
    if (pClVl.getLongs() != null) {
      for (String key : pClVl.getLongs().keySet()) {
        if (!idNms.contains(key)) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
          }
          res.append(key.toUpperCase() + "=" + evSqlVl(pClVl, key));
        }
      }
    }
    if (pClVl.getStrs() != null) {
      for (String key : pClVl.getStrs().keySet()) {
        if (!idNms.contains(key)) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
          }
          res.append(key.toUpperCase() + "=" + evSqlVl(pClVl, key));
        }
      }
    }
    if (pClVl.getInts() != null) {
      for (String key : pClVl.getInts().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pClVl, key));
      }
    }
    if (pClVl.getFloats() != null) {
      for (String key : pClVl.getFloats().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pClVl, key));
      }
    }
    if (pClVl.getDoubles() != null) {
      for (String key : pClVl.getDoubles().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pClVl, key));
      }
    }
    if (pCnd != null) {
      res.append(" where " + pCnd);
    }
    res.append(";");
    return res.toString();
  }

  //Simple getters and setters:
  /**
   * <p>Getter for setng.</p>
   * @return IHld<Class<?, Set<String>>
   **/
  public final ISetng getSetng() {
    return this.setng;
  }

  /**
   * <p>Setter for setng.</p>
   * @param pSetng reference
   **/
  public final void setSetng(
    final ISetng pSetng) {
    this.setng = pSetng;
  }
}
