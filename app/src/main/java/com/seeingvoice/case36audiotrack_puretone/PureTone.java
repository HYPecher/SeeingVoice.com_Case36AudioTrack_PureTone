package com.seeingvoice.case36audiotrack_puretone;

import static com.seeingvoice.case36audiotrack_puretone.GlobalConfig.SAMPLE_RATE;
import static com.seeingvoice.case36audiotrack_puretone.GlobalConfig.AMPLITUDE;

public class PureTone {

	/**
	 * 生成正弦波
	 * 
	 * @param wave //array of value of sin()
	 * @param increment //step
	 *
	 * @return
	 */

	public static short[] sine(short[] wave, double increment) {
		for (int i = 0; i < SAMPLE_RATE; i++) {

			wave[i] = (short) (AMPLITUDE * Math.sin(increment * i));

//            wave[i] = (short) (HEIGHT * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
//            wave[i] = (short) (HEIGHT * (1 - Math.sin(TWOPI * ((i % waveLen) * 1.00 / waveLen))));
		}
		return wave;
	}
}