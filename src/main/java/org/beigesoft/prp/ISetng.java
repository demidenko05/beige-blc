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

import java.util.List;
import java.util.Map;

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
 * </p>
 *
 * @author Yury Demidenko
 */
public interface ISetng {

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

  /**
   * <p>Key name for ID fields.</p>
   **/
  String KEYIDFLDS = "idFlds";

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
  String CMNSTFLNM = "cmnst.xml";

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
   * <p>Lazy gets field's setting for given class, field and name.
   * Maybe NULL.</p>
   * @param pCls class
   * @param pFldNm field name
   * @param pStgNm setting name
   * @return String setting, maybe NULL
   * @throws Exception - an exception
   **/
  String lazFldStg(Class<?> pCls, String pFldNm,
    String pStgNm) throws Exception;

  /**
   * <p>Lazy gets class's setting for given class and name. Maybe NULL.</p>
   * @param pCls class
   * @param pStgNm setting name
   * @return String setting, maybe NULL
   * @throws Exception - an exception
   **/
  String lazClsStg(Class<?> pCls, String pStgNm) throws Exception;

  /**
   * <p>Lazy gets fields names for given class excluding ID, collections and
   * excluded fields by XML file.</p>
   * @param pCls class
   * @return fields names set
   * @throws Exception - an exception
   **/
  List<String> lazFldNms(Class<?> pCls) throws Exception;

  /**
   * <p>Lazy gets ID fields names for given class.</p>
   * @param pCls class
   * @return fields names set
   * @throws Exception - an exception
   **/
  List<String> lazIdFldNms(Class<?> pCls) throws Exception;

  /**
   * <p>Lazy gets all involved classes.</p>
   * @return classes list
   * @throws Exception - an exception
   **/
  List<Class<?>> lazClss() throws Exception;

  /**
   * <p>Lazy gets common settings.</p>
   * @return common settings if exists or empty map
   * @throws Exception - an exception
   **/
  Map<String, String> lazCmnst() throws Exception;

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

  //for requestors that transform properties and hold them by themselves:
  /**
   * <p>Getter for cmnStgs.</p>
   * @return Map<String, String>
   **/
  Map<String, String> getCmnStgs();

  /**
   * <p>Getter for clsStgs.</p>
   * @return Map<Class<?>, Map<String, String>>
   **/
  Map<Class<?>, Map<String, String>> getClsStgs();

  /**
   * <p>Getter for fldStgs.</p>
   * @return Map<Class<?>, Map<String, Map<String, String>>>
   **/
  Map<Class<?>, Map<String, Map<String, String>>> getFldStgs();
}
