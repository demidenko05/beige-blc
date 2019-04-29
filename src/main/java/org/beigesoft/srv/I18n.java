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

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.beigesoft.log.ILog;

/**
 * <p>Standard implementation of I18N service.
 * Use native2ascii -encoding UTF-8 [in-file] [out-file]
 * to convert from UTF-8 to ISO-8859-1.</p>
 *
 * @author Yury Demidenko
 */
public class I18n implements II18n {

  /**
   * <p>Log.</p>
   **/
  private ILog log;

  /**
   * <p>Messages for default or the first language.</p>
   **/
  private ResourceBundle messages;

  /**
   * <p>Messages for language map.</p>
   **/
  private final Map<String, ResourceBundle> messagesMap =
    new HashMap<String, ResourceBundle>();

  /**
   * <p>Default initializer that load message bundle by default locale.</p>
   **/
  public final void initDefault() {
    try {
      this.messages = null;
      this.messages = ResourceBundle.getBundle("Msgs");
    } catch (Exception e) {
      this.log.error(null, I18n.class,
        " when loading msgs for default locale ", e);
    }
    if (messages != null) {
      this.messagesMap.put(Locale.getDefault().getLanguage(), this.messages);
      this.log.info(null, I18n.class,
        "Added messages for default locale: " + Locale.getDefault());
    } else {
      //If there is no MessagesBundle[current-locale].properties
      this.log.error(null, I18n.class,
        "There is no messages for current locale: " + Locale.getDefault());
    }
  }

  /**
   * <p>Add message bundles by given language/country names.</p>
   * @param pLangCountries array of language/country names,
   *   e.g. ["en", "US", "ru", "RU"]
   **/
  public final void add(final String[] pLangCountries) {
    if (pLangCountries != null && pLangCountries.length % 2 == 0) {
      for (int i = 0; i < pLangCountries.length / 2; i++) {
        Locale locale = new Locale(pLangCountries[i * 2],
          pLangCountries[i * 2 + 1]);
        if (!(messages != null && locale.equals(Locale.getDefault()))) {
          ResourceBundle msgs = null;
          try {
            msgs = ResourceBundle.getBundle("Msgs", locale);
          } catch (Exception e) {
            this.log.error(null, I18n.class,
              " when loading msg for locale " + locale, e);
          }
          if (msgs != null) {
            this.messagesMap.put(pLangCountries[i * 2], msgs);
            this.log.info(null, I18n.class,
              "Added messages for lang/country: " + pLangCountries[i * 2]
                + "/" + pLangCountries[i * 2 + 1]);
            if (this.messages == null) {
              this.messages = msgs;
            }
          } else {
            //If there is no MessagesBundle[current-locale].properties
            this.log.error(null, I18n.class,
              "There is no messages for lang/country: " + pLangCountries[i * 2]
                + "/" + pLangCountries[i * 2 + 1]);
          }
        } else {
          this.log.info(null, I18n.class,
            "Messages already added as default for lang/country: "
              + pLangCountries[i * 2] + "/" + pLangCountries[i * 2 + 1]);
        }
      }
    } else {
      String msg = null;
      if (pLangCountries == null) {
        msg =  "is null!";
      } else {
        for (String str : pLangCountries) {
          if (msg == null) {
            msg = str;
          } else {
            msg += "/" + str;
          }
        }
      }
      this.log.error(null, I18n.class,
        "Parameters language error, pLangCountries: " + msg);
    }
  }

  /**
   * <p>Evaluate message by given key for default, the first language
   * or preferred language stored as thread local variable.</p>
   * @param key of message
   **/
  @Override
  public final String getMsg(final String key) {
    try {
      return messages.getString(key);
    } catch (Exception e) {
      return "[" + key + "]";
    }
  }

  /**
   * <p>Evaluate message by given key for given language.</p>
   * @param pKey key of message
   * @param pLang e.g. "en", "ru", etc.
   **/
  @Override
  public final String getMsg(final String pKey, final String pLang) {
    try {
      ResourceBundle mb = messagesMap.get(pLang);
      if (mb != null) {
        return mb.getString(pKey);
      } else {
        return getMsg(pKey);
      }
    } catch (Exception e) {
      return "[" + pKey + "]-" + pLang;
    }
  }

  //Simple getters and setters:
  /**
   * <p>Geter for log.</p>
   * @return ILog
   **/
  public final ILog getLog() {
    return this.log;
  }

  /**
   * <p>Setter for log.</p>
   * @param pLog reference
   **/
  public final void setLog(final ILog pLog) {
    this.log = pLog;
  }


  /**
   * <p>Getter for messages.</p>
   * @return ResourceBundle
   **/
  public final ResourceBundle getMessages() {
    return this.messages;
  }

  /**
   * <p>Setter for messages.</p>
   * @param pMessages reference
   **/
  public final void setMessages(final ResourceBundle pMessages) {
    this.messages = pMessages;
  }

  /**
   * <p>Getter for messagesMap.</p>
   * @return final Map<String, ResourceBundle>
   **/
  public final Map<String, ResourceBundle> getMessagesMap() {
    return this.messagesMap;
  }
}
