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

package org.beigesoft.srv;

import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

import org.beigesoft.exc.ExcCode;
import org.beigesoft.cmp.CmpCsvCol;
import org.beigesoft.hld.IHldEx;
import org.beigesoft.mdl.ECsvClTy;
import org.beigesoft.mdlp.CsvMth;
import org.beigesoft.mdlp.CsvCl;
import org.beigesoft.mdlp.MaFrn;
import org.beigesoft.mdlp.MaFrnLn;

/**
 * <p>Basic writing service that writes CSV data into given stream according
 * given data table and CSV method.
 * Line ending is MS Windows standard - CRLF.</p>
 *
 * @author Yury Demidenko
 */
public class CsvWri implements ICsvWri {

  /**
   * <p>Reflection service.</p>
   **/
  private IReflect reflect;

  /**
   * <p>Number To String service.</p>
   **/
  private INumStr numStr;

  /**
   * <p>Match Foreign holder.</p>
   **/
  private IHldEx<Long, MaFrn> hlMaFrn;

  /**
   * <p>Writes CSV file according given data table and CSV method.
   * Line ending is MS Windows standard - CRLF.</p>
   * @param pRqVs request scoped vars
   * @param pData data table
   * @param pCsMh Csv Method
   * @param pOus servlet/file output stream
   * @throws Exception an Exception
   **/
  @Override
  public final void write(final Map<String, Object> pRqVs,
    final List<List<Object>> pData, final CsvMth pCsMh,
      final OutputStream pOus) throws Exception {
    Map<Long, MaFrn> mfMap = new HashMap<Long, MaFrn>();
    Map<Integer, SimpleDateFormat> dtFrmts =
      new HashMap<Integer, SimpleDateFormat>();
    Map<Integer, String[]> numSps = new HashMap<Integer, String[]>();
    for (CsvCl col : pCsMh.getClns()) {
      if (col.getFrmt() != null) {
        if (col.getTyp().equals(ECsvClTy.DATE)) {
          try {
            dtFrmts.put(col.getIndx(), new SimpleDateFormat(col.getFrmt()));
          } catch (Exception ee) {
            throw new ExcCode(ExcCode.WRCN, "Wrong date format! Format: "
              + col.getFrmt(), ee);
          }
        } else if (col.getTyp().equals(ECsvClTy.NUMERIC)) {
          String[] seps = null;
          try {
            seps = col.getFrmt().split(",");
            for (int i = 0; i < 2; i++) {
              if ("SPACE".equals(seps[i])) {
                seps[i] = "\u00A0";
              } else if ("COMMA".equals(seps[i])) {
                seps[i] = ",";
              }
            }
            numSps.put(col.getIndx(), seps);
          } catch (Exception ee) {
            throw new ExcCode(ExcCode.WRCN, "Wrong amount format! Format: "
              + col.getFrmt(), ee);
          }
        }
      }
    }
    Collections.sort(pCsMh.getClns(), new CmpCsvCol());
    OutputStreamWriter writer = null;
    try {
      writer = new OutputStreamWriter(pOus, Charset
        .forName(pCsMh.getChrst()).newEncoder());
      if (pCsMh.getHasHd()) {
        for (int i = 0; i < pCsMh.getClns().size(); i++) {
          if (i == 0) {
            writer.write(pCsMh.getClns().get(i).getNme());
          } else if (i == pCsMh.getClns().size() - 1) {
            writer.write(pCsMh.getClSp() + pCsMh.getClns()
              .get(i).getNme() + "\r\n");
          } else {
            writer.write(pCsMh.getClSp() + pCsMh.getClns().get(i).getNme());
          }
        }
      }
      for (List<Object> row : pData) {
        for (int i = 0; i < pCsMh.getClns().size(); i++) {
          String clVl = ""; //null default
          if (pCsMh.getClns().get(i).getCnst() != null) {
            clVl = pCsMh.getClns().get(i).getCnst();
          } else if (pCsMh.getClns().get(i).getDtIdx() != null
            && row.get(pCsMh.getClns().get(i).getDtIdx() - 1) != null) {
            Object obVl = null;
            if (pCsMh.getClns().get(i).getFldPh() == null) {
              obVl = row.get(pCsMh.getClns().get(i).getDtIdx() - 1);
            } else {
              String[] fpa = pCsMh.getClns().get(i).getFldPh().split(",");
              Object ent = row.get(pCsMh.getClns().get(i).getDtIdx() - 1);
              for (int j = 0; j < fpa.length; j++) {
                Method getter = this.reflect.retGet(ent.getClass(), fpa[j]);
                obVl = getter.invoke(ent);
                ent = obVl;
                if (ent == null) {
                  break;
                }
              }
            }
            if (obVl != null) {
              if (pCsMh.getClns().get(i).getFrmt() == null) {
                if (pCsMh.getClns().get(i).getMaFrn() != null) {
                  MaFrn mf = mfMap.get(pCsMh.getClns()
                    .get(i).getMaFrn().getIid());
                  if (mf == null) {
                    mf = this.hlMaFrn.get(pRqVs, pCsMh
                      .getClns().get(i).getMaFrn().getIid());
                    if (mf == null) {
                  throw new ExcCode(ExcCode.WRPR, "Can't find match foreign ID "
                      + pCsMh.getClns().get(i).getMaFrn().getIid());
                    }
                    mfMap.put(pCsMh.getClns().get(i).getMaFrn().getIid(), mf);
                  }
                  String natVal = obVl.toString();
                  for (MaFrnLn mfl : mf.getLns()) {
                    if (natVal.equals(mfl.getNtvVl())) {
                      clVl = mfl.getFrnVl();
                      break;
                    }
                  }
                } else {
                  clVl = obVl.toString();
                }
              } else {
                if (pCsMh.getClns().get(i).getTyp().equals(ECsvClTy.DATE)) {
                  clVl = dtFrmts.get(pCsMh.getClns().get(i)
                    .getIndx()).format((Date) obVl);
                } else if (pCsMh.getClns().get(i).getTyp()
                  .equals(ECsvClTy.NUMERIC)) {
                  String[] sps = numSps.get(pCsMh.getClns().get(i).getIndx());
                  Integer scl = null;
                  if (sps.length == 3) {
                    scl = Integer.parseInt(sps[2]);
                  }
                  BigDecimal bdv = (BigDecimal) obVl;
                  if (scl == null) {
                    scl = bdv.scale();
                  } else {
                    bdv = bdv.setScale(scl, RoundingMode.HALF_UP);
                  }
                  clVl = this.numStr.frmt(obVl.toString(), sps[0], sps[1], scl);
                } else if (pCsMh.getClns().get(i).getTyp()
                  .equals(ECsvClTy.BOOLEAN)) {
                  String[] frm = pCsMh.getClns().get(i).getFrmt().split(",");
                  Boolean blv = (Boolean) obVl;
                  if (blv) {
                    clVl = frm[0];
                  } else {
                    clVl = frm[1];
                  }
                } else {
                  throw new ExcCode(ExcCode.WRCN,
                    "data_format_not_yet_implemented");
                }
              }
            }
          }
          if (pCsMh.getClns().get(i).getTxDlm() != null
            && !"".equals(clVl)) {
            clVl = pCsMh.getClns().get(i).getTxDlm()
              + clVl + pCsMh.getClns().get(i).getTxDlm();
          }
          if (i == 0) {
            writer.write(clVl);
          } else if (i == pCsMh.getClns().size() - 1) {
            writer.write(pCsMh.getClSp() + clVl + "\r\n");
          } else {
            writer.write(pCsMh.getClSp() + clVl);
          }
        }
      }
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  //Simple getters and setters:
  /**
   * <p>Getter for reflect.</p>
   * @return IReflect
   **/
  public final IReflect getReflect() {
    return this.reflect;
  }

  /**
   * <p>Setter for reflect.</p>
   * @param pReflect reference
   **/
  public final void setReflect(final IReflect pReflect) {
    this.reflect = pReflect;
  }

  /**
   * <p>Getter for numStr.</p>
   * @return INumStr
   **/
  public final INumStr getNumStr() {
    return this.numStr;
  }

  /**
   * <p>Setter for numStr.</p>
   * @param pNumStr reference
   **/
  public final void setNumStr(final INumStr pNumStr) {
    this.numStr = pNumStr;
  }


  /**
   * <p>Getter for hlMaFrn.</p>
   * @return IHld<Long, MaFrn>
   **/
  public final IHldEx<Long, MaFrn> getHlMaFrn() {
    return this.hlMaFrn;
  }

  /**
   * <p>Setter for hlMaFrn.</p>
   * @param pHlMaFrn reference
   **/
  public final void setHlMaFrn(final IHldEx<Long, MaFrn> pHlMaFrn) {
    this.hlMaFrn = pHlMaFrn;
  }
}
