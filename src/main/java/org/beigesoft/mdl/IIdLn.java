package org.beigesoft.mdl;

/*
 * Copyright (c) 2017 Beigesoft ™
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
 * <p>Abstraction of persistable model with Long ID and version.
 * This is for replicable model, i.e. ID is non-auto-generated.</p>
 *
 * @author Yury Demidenko
 */
public interface IIdLn extends IHasId<Long> {

}
