/*-
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2020 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imglib2.roi.labeling;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedIntType;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

/**
 * @author Matthias Arzt
 */
public class ImgLabelingTest {

	@Test
	public void testCreateFromImageAndLabelSets() {
		// setup
		Img<UnsignedIntType> image = ArrayImgs.unsignedInts(new int[]{1, 0, 2}, 3);
		String[] values = {"A", "B"};
		String[] valuesB = {"Hello", "World"};
		List<Set<String>> labelSets = Arrays.asList(asSet(), asSet(values), asSet(valuesB));
		// process
		ImgLabeling<String, UnsignedIntType> labeling = ImgLabeling.fromImageAndLabelSets(image, labelSets);
		// test
		RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
		ra.setPosition(new long[]{0});
		assertEquals(asSet(values), ra.get());
		ra.setPosition(new long[]{1});
		assertEquals(Collections.emptySet(), ra.get());
		ra.setPosition(new long[]{2});
		assertEquals(asSet(valuesB), ra.get());
	}

	@Test
	public void testCreateFromImageAndLabels() {
		// setup
		Img<UnsignedIntType> image = ArrayImgs.unsignedInts(new int[]{3}, 1);
		List<String> labels = Arrays.asList("a", "b", "c", "d", "e", "f", "g");
		// process
		ImgLabeling<String, UnsignedIntType> labeling = ImgLabeling.fromImageAndLabels(image, labels);
		// test
		RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
		ra.setPosition(new long[]{0});
		assertEquals(asSet("c"), ra.get());
	}

	@Test
	public void testModifyCreatedImgLabeling() {
		// setup
		int[] data = {2};
		Img<UnsignedIntType> image = ArrayImgs.unsignedInts(data, 1);
		List<Set<String>> labelSets = Arrays.asList(asSet(), asSet("1","2"), asSet("1"));
		ImgLabeling<String, UnsignedIntType> labeling = ImgLabeling.fromImageAndLabelSets(image, labelSets);
		RandomAccess<LabelingType<String>> ra = labeling.randomAccess();
		ra.setPosition(new long[]{0});
		// process
		ra.get().add("2");
		// test
		assertEquals(asSet("1", "2"), ra.get());
		assertEquals(1, data[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatedImgLabelingRepeatingLabel() {
		Img<UnsignedIntType> image = ArrayImgs.unsignedInts(new int[]{2}, 1);
		ImgLabeling<String, UnsignedIntType> labeling = ImgLabeling.fromImageAndLabels(image, Arrays.asList("1", "1"));
	}

	private <T> Set<T> asSet(T... values) {
		return new TreeSet<>(Arrays.asList(values));
	}
}