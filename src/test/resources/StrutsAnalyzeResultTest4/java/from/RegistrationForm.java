/*
 * $Id: RegistrationForm.java 471754 2006-11-06 14:55:09Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


package org.apache.struts.webapp.validator;

import java.io.Serializable;
import org.apache.struts.validator.ValidatorForm;

/**
 * 変換用テストデータファイル
 *
 * @author Ko Ho
 */
public class RegistrationForm extends ValidatorForm implements Serializable {

    public String action = null;

    public String firstName = null;
    public String lastName = null;
    public String addr = null;
    public CityStateZip cityStateZip = new CityStateZip();
    public String phone = null;
    public String email = null;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CityStateZip getCityStateZip() {
        return cityStateZip;
    }

    public void setCityStateZip(CityStateZip cityStateZip) {
        this.cityStateZip = cityStateZip;
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset() {
        action = null;
        firstName = null;
        lastName = null;
        addr = null;
        cityStateZip = new CityStateZip();
        phone = null;
        email = null;
    }

}
