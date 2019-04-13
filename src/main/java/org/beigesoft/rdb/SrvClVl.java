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

import java.util.HashSet;
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
   * @param pCv column values
   * @return string representation
   **/
  public final String str(final ColVals pCv) {
    StringBuffer sb = new StringBuffer("IDs column names: ");
    strCv(pCv, sb);
    return sb.toString();
  }

  /**
   * <p>Makes string representation.</p>
   * @param pCls entity class
   * @param pCv column values
   * @return string representation
   * @throws Exception an Exception
   **/
  public final String str(final Class<?> pCls,
    final ColVals pCv) throws Exception {
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
    strCv(pCv, sb);
    return sb.toString();
  }

  /**
   * <p>Makes string representation.</p>
   * @param pCv column values
   * @param pSb StringBuffer
   **/
  private void strCv(final ColVals pCv,
    final StringBuffer pSb) {
    if (pCv.getOldVer() != null) {
      pSb.append("\nOld version=" + pCv.getOldVer());
    }
    boolean isFirst = true;
    if (pCv.getExprs() != null) {
      pSb.append("\nColumns with expression: ");
      for (String clNm : pCv.getExprs()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(clNm);
      }
    }
    pSb.append("\nvalues: ");
    if (pCv.getInts() != null) {
      for (Map.Entry<String, Integer> ent : pCv.getInts().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pCv.getLongs() != null) {
      for (Map.Entry<String, Long> ent : pCv.getLongs().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pCv.getFloats() != null) {
      for (Map.Entry<String, Float> ent : pCv.getFloats().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pCv.getDoubles() != null) {
      for (Map.Entry<String, Double> ent : pCv.getDoubles().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
    if (pCv.getStrs() != null) {
      for (Map.Entry<String, String> ent : pCv.getStrs().entrySet()) {
        if (isFirst) {
          isFirst = false;
        } else {
          pSb.append(",");
        }
        pSb.append(ent.getKey() + "=" + ent.getValue());
      }
    }
  }

  /**
   * <p>Put column name that has expression.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   **/
  public final void putExpr(final ColVals pCv, final String pNm) {
    if (pCv.getExprs() == null) {
      pCv.setExprs(new HashSet<String>());
    }
    pCv.getExprs().add(pNm);
  }

  /**
   * <p>Put into column Long value.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pCv, final String pNm,
    final Long pVal) {
    if (pCv.getLongs() == null) {
      pCv.setLongs(new HashMap<String, Long>());
    }
    pCv.getLongs().put(pNm, pVal);
  }

  /**
   * <p>Put into column String value.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pCv, final String pNm,
    final String pVal) {
    if (pCv.getStrs() == null) {
      pCv.setStrs(new HashMap<String, String>());
    }
    pCv.getStrs().put(pNm, pVal);
  }

  /**
   * <p>Put into column Integer val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pCv, final String pNm,
    final Integer pVal) {
    if (pCv.getInts() == null) {
      pCv.setInts(new HashMap<String, Integer>());
    }
    pCv.getInts().put(pNm, pVal);
  }

  /**
   * <p>Put into column Float val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pCv, final String pNm,
    final Float pVal) {
    if (pCv.getFloats() == null) {
      pCv.setFloats(new HashMap<String, Float>());
    }
    pCv.getFloats().put(pNm, pVal);
  }

  /**
   * <p>Put into column Double val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @param pVal column val
   **/
  public final void put(final ColVals pCv, final String pNm,
    final Double pVal) {
    if (pCv.getDoubles() == null) {
      pCv.setDoubles(new HashMap<String, Double>());
    }
    pCv.getDoubles().put(pNm, pVal);
  }

  /**
   * <p>Get column with Integer val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Integer column val
   * @throws ExcCode if field not found
   **/
  public final Integer getInt(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getInts() != null && pCv.getInts().keySet().contains(pNm)) {
      return pCv.getInts().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Get column with Long value or ID.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Long column val
   * @throws ExcCode if field not found
   **/
  public final Long getLong(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getLongs() != null && pCv.getLongs().keySet().contains(pNm)) {
      return pCv.getLongs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Get column with String val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return String column val
   * @throws ExcCode if field not found
   **/
  public final String getString(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getStrs() != null && pCv.getStrs().keySet().contains(pNm)) {
      return pCv.getStrs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Get ID column with String or Long value to check if null.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Object column val
   * @throws ExcCode if field not found
   **/
  public final Object getIdVl(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getLongs() != null && pCv.getLongs().keySet().contains(pNm)) {
      return pCv.getLongs().get(pNm);
    }
    if (pCv.getStrs() != null && pCv.getStrs().keySet().contains(pNm)) {
      return pCv.getStrs().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Get column with Float val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Float column val
   * @throws ExcCode if field not found
   **/
  public final Float getFloat(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getFloats() != null && pCv.getFloats().keySet().contains(pNm)) {
      return pCv.getFloats().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Get column with Double val.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Double column val
   * @throws ExcCode if field not found
   **/
  public final Double getDouble(final ColVals pCv,
    final String pNm) throws ExcCode {
  if (pCv.getDoubles() != null && pCv.getDoubles().keySet().contains(pNm)) {
      return pCv.getDoubles().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Evaluate column val for SQL statement INSERT or UPDATE.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return String column val
   * @throws ExcCode if column not found
   **/
  public final String evSqlVl(final ColVals pCv,
    final String pNm) throws ExcCode {
    Object val = evObjVl(pCv, pNm);
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
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return Object column val
   * @throws ExcCode if column not found
   **/
  public final Object evObjVl(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getInts() != null && pCv.getInts().keySet().contains(pNm)) {
      return pCv.getInts().get(pNm);
    }
    if (pCv.getLongs() != null && pCv.getLongs().keySet().contains(pNm)) {
      return pCv.getLongs().get(pNm);
    }
    if (pCv.getStrs() != null && pCv.getStrs().keySet().contains(pNm)) {
      String val = pCv.getStrs().get(pNm);
      if (val == null) {
        return null;
      } else {
        if (pCv.getExprs() != null && pCv.getExprs().contains(pNm)) {
          return val;
        } else {
          return "'" + val + "'";
        }
      }
    }
    if (pCv.getFloats() != null && pCv.getFloats().keySet().contains(pNm)) {
      return pCv.getFloats().get(pNm);
    }
    if (pCv.getDoubles() != null
      && pCv.getDoubles().keySet().contains(pNm)) {
      return pCv.getDoubles().get(pNm);
    }
    throw new ExcCode(ExcCode.WRPR, "There is no field/CV - " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Check whether column is contained here.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @return if contains
    **/
  public final boolean contains(final ColVals pCv, final String pNm) {
    if (pCv.getInts() != null && pCv.getInts().keySet().contains(pNm)) {
      return true;
    }
    if (pCv.getLongs() != null && pCv.getLongs().keySet().contains(pNm)) {
      return true;
    }
    if (pCv.getStrs() != null && pCv.getStrs().keySet().contains(pNm)) {
      return true;
    }
    if (pCv.getFloats() != null && pCv.getFloats().keySet().contains(pNm)) {
      return true;
    }
  if (pCv.getDoubles() != null && pCv.getDoubles().keySet().contains(pNm)) {
      return true;
    }
    return false;
  }

  /**
   * <p>Remove column.</p>
   * @param pCv type-safe map column name - column value
   * @param pNm column name
   * @throws ExcCode if column not found
   **/
  public final void remove(final ColVals pCv,
    final String pNm) throws ExcCode {
    if (pCv.getInts() != null && pCv.getInts().keySet().contains(pNm)) {
      pCv.getInts().remove(pNm);
      return;
    }
    if (pCv.getLongs() != null && pCv.getLongs().keySet().contains(pNm)) {
      pCv.getLongs().remove(pNm);
      return;
    }
    if (pCv.getStrs() != null && pCv.getStrs().keySet().contains(pNm)) {
      pCv.getStrs().remove(pNm);
      return;
    }
    if (pCv.getFloats() != null && pCv.getFloats().keySet().contains(pNm)) {
      pCv.getFloats().remove(pNm);
      return;
    }
    if (pCv.getDoubles() != null
      && pCv.getDoubles().keySet().contains(pNm)) {
      pCv.getDoubles().remove(pNm);
      return;
    }
    throw new ExcCode(ExcCode.WRPR, "There is no column/CV " + pNm
      + "/" + str(pCv));
  }

  /**
   * <p>Evaluate SQL insert statement. It's assumed that ID maybe either Long
   * or String type, i.e. Integer is not.</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @return insert query
   * @throws Exception - an exception
   **/
  public final String evInsert(final Class<?> pCls,
    final ColVals pCv) throws Exception {
    StringBuffer res = new StringBuffer("insert into "
      + pCls.getSimpleName().toUpperCase() + " (");
    StringBuffer vls = new StringBuffer(" values (");
    boolean isFst = true;
    List<String> idNms = this.setng.lazIdFldNms(pCls);
    //ID-able Long and String:
    if (pCv.getLongs() != null) {
      for (Map.Entry<String, Long> ent : pCv.getLongs().entrySet()) {
        if (ent.getValue() != null || !idNms.contains(ent.getKey())) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
            vls.append(", ");
          }
          res.append(ent.getKey().toUpperCase());
          vls.append(evSqlVl(pCv, ent.getKey()));
        }
      }
    }
    if (pCv.getStrs() != null) {
      for (Map.Entry<String, String> ent : pCv.getStrs().entrySet()) {
        if (ent.getValue() != null || !idNms.contains(ent.getKey())) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
            vls.append(", ");
          }
          res.append(ent.getKey().toUpperCase());
          vls.append(evSqlVl(pCv, ent.getKey()));
        }
      }
    }
    if (pCv.getInts() != null) {
      for (Map.Entry<String, Integer> ent : pCv.getInts().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pCv, ent.getKey()));
      }
    }
    if (pCv.getFloats() != null) {
      for (Map.Entry<String, Float> ent : pCv.getFloats().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pCv, ent.getKey()));
      }
    }
    if (pCv.getDoubles() != null) {
      for (Map.Entry<String, Double> ent : pCv.getDoubles().entrySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
          vls.append(", ");
        }
        res.append(ent.getKey().toUpperCase());
        vls.append(evSqlVl(pCv, ent.getKey()));
      }
    }
    res.append(") " + vls + ");");
    return res.toString();
  }

  /**
   * <p>Evaluate where conditions for SQL update standard statement
   * with optimistic locking if need.</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @return where conditions e.g. "IID=1 and VER=2"
   * @throws Exception - an exception
   **/
  public final String evWheUpd(final Class<?> pCls,
    final ColVals pCv) throws Exception {
    StringBuffer sb = new StringBuffer("");
    boolean isFst = true;
    for (String idNm : this.setng.lazIdFldNms(pCls)) {
      if (isFst) {
        isFst = false;
      } else {
        sb.append(" and ");
      }
      sb.append(idNm.toUpperCase() + "=" + evSqlVl(pCv, idNm));
    }
    if (pCv.getOldVer() != null) {
      sb.append(" and VER=" + pCv.getOldVer());
    }
    return sb.toString();
  }

  /**
   * <p>Evaluate SQL update standard statement
   * with optimistic locking if need.</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @return update statement
   * @throws Exception - an exception
   **/
  public final String evUpdate(final Class<?> pCls,
    final ColVals pCv) throws Exception {
    String cnd = evWheUpd(pCls, pCv);
    return evUpdateCnd(pCls, pCv, cnd);
  }

  /**
   * <p>Evaluate SQL update statement with given conditions (nullable).
   * It's assumed that ID maybe either Long or String type!</p>
   * @param pCls entity class
   * @param pCv type-safe map column name - column value
   * @param pCnd where conditions e.g. "IID=2 and VER=2" or NULL
   * @return update statement
   * @throws Exception - an exception
   **/
  public final String evUpdateCnd(final Class<?> pCls,
    final ColVals pCv, final String pCnd) throws Exception {
    StringBuffer res = new StringBuffer("update "
      + pCls.getSimpleName().toUpperCase() + " set ");
    List<String> idNms = this.setng.lazIdFldNms(pCls);
    boolean isFst = true;
    if (pCv.getLongs() != null) {
      for (String key : pCv.getLongs().keySet()) {
        if (!idNms.contains(key)) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
          }
          res.append(key.toUpperCase() + "=" + evSqlVl(pCv, key));
        }
      }
    }
    if (pCv.getStrs() != null) {
      for (String key : pCv.getStrs().keySet()) {
        if (!idNms.contains(key)) {
          if (isFst) {
            isFst = false;
          } else {
            res.append(", ");
          }
          res.append(key.toUpperCase() + "=" + evSqlVl(pCv, key));
        }
      }
    }
    if (pCv.getInts() != null) {
      for (String key : pCv.getInts().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pCv, key));
      }
    }
    if (pCv.getFloats() != null) {
      for (String key : pCv.getFloats().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pCv, key));
      }
    }
    if (pCv.getDoubles() != null) {
      for (String key : pCv.getDoubles().keySet()) {
        if (isFst) {
          isFst = false;
        } else {
          res.append(", ");
        }
        res.append(key.toUpperCase() + "=" + evSqlVl(pCv, key));
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
