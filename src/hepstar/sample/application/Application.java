/*
 * Author henry
 *
 * Copyright (c) 2025 Hepstar, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Hepstar. ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Hepstar
 *
 * HEPSTAR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package hepstar.sample.application;

/**
 * @author henry
 *
 */
public class Application {

	// region Private Members

	/**
	 * This method loads the insured details from the insured.txt file
	 * 
	 * @return
	 */
	private static Insured getInsured() {
		Insured insured = new Insured();

		// TODO: Load insured details from insured.txt

		return insured;
	}

	/**
	 * This method loads the travel information details from the travelinformation.txt file
	 * 
	 * @return
	 */
	private static TravelInformation getTravelInformation() {
		TravelInformation travelinformation = new TravelInformation();

		// TODO: Load travel information details from travelinformation.txt

		return travelinformation;
	}

	/**
	 * This method loads the request message from the request.xml file
	 * 
	 * @return
	 */
	private static String getRequestMessage(Insured insured, TravelInformation travelinformation) {
		String requestmessage = "";

		// TODO: Load the request.xml file into requestmessage variable
		// TODO: replace all {DATA} with data from the getter methods from either insured or travel information
		// TODO: make sure {SESSION} is replaced with a GUID

		return requestmessage;
	}

	private static String sendRequestMessage(String requestmessage) {
		// TODO: send the request message as an HTTPs POST to our endpoint and receive a response
	}

	// endregion

	// region Public Members

	/**
	 * This is where is all begins
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Insured insured = getInsured();
		TravelInformation travelinformation = getTravelInformation();
		String requestmessage = getRequestMessage(insured, travelinformation);

		System.out.println(requestmessage);

		String responsemessage = sendRequestMessage(requestmessage);

		System.out.println(responsemessage);
	}

	// endregion

}
