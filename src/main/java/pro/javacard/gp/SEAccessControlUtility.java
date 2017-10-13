/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 Bertrand Martel
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pro.javacard.gp;


/**
 * SE Access Control utility.
 */
public final class SEAccessControlUtility {

	/**
	 * Send Access Control rule GET DATA.
	 *
	 * @param card
	 * @param P1
	 * @return
	 * @throws CardExceptionWrapper
	 * @throws GPException
	 */
	private static ResponseAPDUWrapper sendAcrGetData(final CardWrapper card, final byte P1) throws CardExceptionWrapper, GPException {
		CommandAPDUWrapper list = new CommandAPDUWrapper(GlobalPlatform.CLA_GP, GlobalPlatform.INS_GET_DATA, 0xFF, P1, 256);

		ResponseAPDUWrapper response = new CardChannelWrapper(card.getBasicChannel()).transmit(list);

		try {
			GPException.check(response, "ACR GET DATA failed");
		} catch (GPException e) {
			if (SEAccessControl.ACR_GET_DATA_ERROR.containsKey(e.sw)) {
				System.out.println("[SW] " + SEAccessControl.ACR_GET_DATA_ERROR.get(e.sw));
			}
			throw e;
		}
		return response;
	}

	/**
	 * List access rules.
	 *
	 * @param card
	 * @throws CardExceptionWrapper
	 * @throws GPException
	 */
	public static void acrList(final GlobalPlatform gp, final CardWrapper card) throws CardExceptionWrapper, GPException {
		boolean selected = gp.select(SEAccessControl.ACR_AID);
		if (selected) {
			ResponseAPDUWrapper response = sendAcrGetData(card, SEAccessControl.ACR_GET_DATA_ALL);
			SEAccessControl.BerTlvData temp = SEAccessControl.AcrListResponse.getAcrListData(null, response.getData());

			while (temp.getCurrentIndex() < temp.getLength()) {
				response = sendAcrGetData(card, SEAccessControl.ACR_GET_DATA_NEXT);
				temp = SEAccessControl.AcrListResponse.getAcrListData(temp, response.getData());
			}

			SEAccessControl.AcrListResponse resp = SEAccessControl.AcrListResponse.fromBytes(temp.getLength(), temp.getData());
			SEAccessControl.printList(resp.acrList);
		} else {
			throw new GPException("no AID " + SEAccessControl.ACR_AID + " found");
		}
	}

	/**
	 * Add an access rule.
	 *
	 * @param aid
	 * @param hash
	 * @param rules
	 * @throws CardExceptionWrapper
	 * @throws GPException
	 */
	public static void acrAdd(final GlobalPlatform gp, AID aid, final byte[] hash, final byte[] rules) throws CardExceptionWrapper, GPException {
		SEAccessControl.RefArDo refArDo = new SEAccessControl.RefArDo(aid, hash, rules);
		SEAccessControl.StoreArDo storeArDo = new SEAccessControl.StoreArDo(refArDo);
		acrStore(gp, storeArDo);
	}

	/**
	 * Send store data for access rule.
	 *
	 * @param data TLV data
	 * @throws CardExceptionWrapper
	 * @throws GPException
	 */
	public static void acrStore(final GlobalPlatform gp, final SEAccessControl.TLV data) throws CardExceptionWrapper, GPException {
		try {
			//0x90 is for getting BER-TLV data (Secure Element Access Control v1.0 p36)
			gp.storeData(SEAccessControl.ACR_AID, data.getBytes(), (byte) 0x90);
		} catch (GPException e) {
			if (SEAccessControl.ACR_STORE_DATA_ERROR.containsKey(e.sw)) {
				System.out.println("[SW] " + SEAccessControl.ACR_STORE_DATA_ERROR.get(e.sw));
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	 * Delete an access rule by AID/HASH.
	 *
	 * @param aid
	 * @param hash
	 * @throws CardExceptionWrapper
	 * @throws GPException
	 */
	public static void acrDelete(final GlobalPlatform gp, final AID aid, final byte[] hash) throws CardExceptionWrapper, GPException {
		SEAccessControl.TLV request;

		if (hash != null) {
			SEAccessControl.RefArDo refArDo = new SEAccessControl.RefArDo(aid, hash);
			request = new SEAccessControl.DeleteArDo(refArDo);
		} else {
			SEAccessControl.AidRefDo aidRefDo = new SEAccessControl.AidRefDo(aid.getBytes());
			request = new SEAccessControl.DeleteAidDo(aidRefDo);
		}
		acrStore(gp, request);
	}
}
