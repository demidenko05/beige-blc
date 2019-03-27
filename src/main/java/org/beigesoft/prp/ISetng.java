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

package org.beigesoft.prp;

import java.util.Set;

import org.beigesoft.hld.IHld;

/**
 * <p>Abstraction of service that loads classes and their fields settings
 * from XML property file.
 * Priority of XML classes properties:
 * <pre>
 * 1. clsCs - class to settings
 * 5. clsTyCs - class type to settings
 * </pre>
 * Priority of XML fields properties:
 * <pre>
 * 1. clsFs - class to settings
 * 2. fldNmClsTyFs - field name class type to settings
 * 3. fldNmTyFs - field name and type to settings
 * 4. fldNmFs - field name to settings
 * 5. fldTyFs - field type to settings
 * </pre>
 * It is also holder of involved classes involved fields names.
 * </p>
 *
 * @author Yury Demidenko
 */
public interface ISetng extends IHld<Class<?>, Set<String>> {

  //Keys for configuration file:
  /**
   * <p>Key name for classes.</p>
   **/
  String KEYCLSS = "clss";

  /**
   * <p>Key name for fields settings names.</p>
   **/
  String KEYCLSSTNMS = "stgClsNms";

  /**
   * <p>Key name for fields settings names.</p>
   **/
  String KEYFLDSTNMS = "stgFldNms";

  /**
   * <p>Key name for exclude fields.</p>
   **/
  String KEYEXLFLDS = "exlFlds";

  //Files, dirs names:
    //Configuration:
  /**
   * <p>App setting file name.</p>
   **/
  String CONFFLNM = "conf.xml";

    //Common settings:
  /**
   * <p>Common settings file name.</p>
   **/
  String APPSTFLNM = "cmnst.xml";

    //Classes settings:
  /**
   * <p>Folder class type to classes setting.</p>
   **/
  String DIRCLSTYCS = "clsTyCs";

  /**
   * <p>Folder class to class's setting.</p>
   **/
  String DIRCLSCS = "clsCs";

    //Fields settings:
  /**
   * <p>Folder field type to field's setting.</p>
   **/
  String DIRFLDTYFS = "fldTyFs";

  /**
   * <p>Folder field name field type to field's setting.</p>
   **/
  String DIRFLDNMTYFS = "fldNmTyFs";

  /**
   * <p>Folder field name class type to field's setting.</p>
   **/
  String DIRFLDNMCLSTYFS = "fldNmClsTyFs";

  /**
   * <p>Folder field name to field's setting.</p>
   **/
  String DIRFLDNMFS = "fldNmFs";

  /**
   * <p>Folder class to fields setting.</p>
   **/
  String DIRCLSFS = "clsFs";

  /**
   * <p>Lazy gets fields setting by given name. Maybe NULL, e.g. converter
   * name for fields of standard type (resource friendly approach).
   * It's also maybe only class settings configuration.</p>
   * @param pCls class
   * @param pFldNm field name
   * @param pStgNm setting name
   * @return String setting, maybe NULL
   * @throws Exception - an exception
   **/
  String lazFldStg(Class<?> pCls, String pFldNm,
    String pStgNm) throws Exception;

  /**
   * <p>Lazy gets fields names for given name excluding collections and
   * excluded fields by XML file.</p>
   * @param pCls class
   * @return fields names set
   * @throws Exception - an exception
   **/
  Set<String> lazFldNms(Class<?> pCls) throws Exception;

  /**
   * <p>Releases beans (memory).</p>
   * @throws Exception - an exception
   **/
  void release() throws Exception;

    //configuration:
  /**
   * <p>Getter for base setting directory.</p>
   * @return String
   **/
  String getDir();

  /**
   * <p>Setter for base setting directory.</p>
   * @param pDir reference
   **/
  void setDir(String pDir);
}
