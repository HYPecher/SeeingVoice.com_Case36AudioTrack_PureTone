package com.seeingvoice.case36audiotrack_puretone;

import static com.seeingvoice.case36audiotrack_puretone.GlobalConfig.SAMPLE_RATE;
import static com.seeingvoice.case36audiotrack_puretone.GlobalConfig.AMPLITUDE;

public class PureTone {

	public static short[] sine(short[] wave, double increment) {
		for (int i = 0; i < SAMPLE_RATE; i++) {
			wave[i] = (short) (AMPLITUDE * Math.sin(increment * i));
		}
		return wave;
	}
}