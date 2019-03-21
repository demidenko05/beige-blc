package org.beigesoft.mdlp;

/*
 * Copyright (c) 2018 Beigesoft ™
 *
 * Licensed under the GNU General Public License (GPL), Version 2.0
 * (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

/**
 * <p>
 * Composite ID of I18N preferences for a country.
 * </p>
 *
 * @author Yury Demidenko
 */
public class UsPrfId {

  /**
   * <p>Country.</p>
   **/
  private Cntr cntr;

  /**
   * <p>Language.</p>
   **/
  private Lng lng;

  //Simple getters and setters:
  /**
   * <p>Getter for cntr.</p>
   * @return Cntr
   **/
  public final Cntr getCntr() {
    return this.cntr;
  }

  /**
   * <p>Setter for cntr.</p>
   * @param pCntr reference
   **/
  public final void setCntr(final Cntr pCntr) {
    this.cntr = pCntr;
  }

  /**
   * <p>Getter for lng.</p>
   * @return Lng
   **/
  public final Lng getLng() {
    return this.lng;
  }

  /**
   * <p>Setter for lng.</p>
   * @param pLng reference
   **/
  public final void setLng(final Lng pLng) {
    this.lng = pLng;
  }
}
