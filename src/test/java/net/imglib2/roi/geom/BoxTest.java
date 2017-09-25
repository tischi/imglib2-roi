/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
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
package net.imglib2.roi.geom;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.imglib2.RealPoint;
import net.imglib2.roi.geom.real.Box;
import net.imglib2.roi.geom.real.ClosedBox;
import net.imglib2.roi.geom.real.OpenBox;
import net.imglib2.roi.mask.Mask.BoundaryType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Box}.
 *
 * @author Alison Walter
 */
public class BoxTest
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testTwoDimensionalOpenRectangle()
	{
		final Box b = new OpenBox( new double[] { -6.8, -3.2375 }, new double[] { 13.2, 3.2625 } );

		// vertices
		assertFalse( b.test( new RealPoint( new double[] { -6.8, -3.2375 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { -6.8, 3.2625 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 13.2, -3.2375 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 13.2, 3.2625 } ) ) );

		// inside
		assertTrue( b.test( new RealPoint( new double[] { 0, 0 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 9, 1.625 } ) ) );

		// outside
		assertFalse( b.test( new RealPoint( new double[] { -6.8, 3.25 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 13.2, 0 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 11, 3.2625 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { -4, -3.2375 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 12, 20 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { -6.81, 0 } ) ) );

		// box characteristics
		assertEquals( b.sideLength( 0 ), 20, 0 );
		assertEquals( b.sideLength( 1 ), 6.5, 0 );
		assertEquals( b.center()[ 0 ], 3.2, 1e-15 );
		assertEquals( b.center()[ 1 ], 0.0125, 1e-15 );
		assertTrue( b.boundaryType() == BoundaryType.OPEN );
	}

	@Test
	public void testTwoDimensionalClosedRectangle()
	{
		final Box b = new ClosedBox( new double[] { -6.8, -3.2375 }, new double[] { 13.2, 3.2625 } );

		// vertices
		assertTrue( b.test( new RealPoint( new double[] { -6.8, -3.2375 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { -6.8, 3.2625 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 13.2, -3.2375 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 13.2, 3.2625 } ) ) );

		// inside
		assertTrue( b.test( new RealPoint( new double[] { -6.8, 3.25 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 13.2, 0 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 11, 3.2625 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { -4, -3.2375 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 0, 0 } ) ) );

		// outside
		assertFalse( b.test( new RealPoint( new double[] { 12, 20 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { -6.81, 0 } ) ) );

		// box characteristics
		assertEquals( b.sideLength( 0 ), 20, 0 );
		assertEquals( b.sideLength( 1 ), 6.5, 0 );
		assertEquals( b.center()[ 0 ], 3.2, 1e-15 );
		assertEquals( b.center()[ 1 ], 0.0125, 1e-15 );
		assertTrue( b.boundaryType() == BoundaryType.CLOSED );
	}

	@Test
	public void testHighDimensionalOpenBox()
	{
		final Box hc = new OpenBox( new double[] { 3, 3, 3, 3 }, new double[] { 7, 7, 7, 7 } );

		// vertices
		assertFalse( hc.test( new RealPoint( new double[] { 3, 3, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 3, 3, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 3, 7, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 3, 7, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 7, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 7, 3, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 7, 7, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 7, 7, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 3, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 3, 3, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 3, 7, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 3, 7, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 7, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 7, 3, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 7, 7, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 7, 7, 7 } ) ) );

		// should contain:
		assertTrue( hc.test( new RealPoint( new double[] { 4, 4, 4, 4 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 5, 6, 4, 5 } ) ) );

		// should not contain:
		assertFalse( hc.test( new RealPoint( new double[] { 7, 3, 5, 4 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 7, 7, 4, 4 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 6, 5, 5 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 6, 6, 5, 7 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 2, 3, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 2, 3, 3 } ) ) );

		// box characteristics
		assertEquals( hc.sideLength( 0 ), 4, 0 );
		assertEquals( hc.sideLength( 1 ), 4, 0 );
		assertEquals( hc.sideLength( 2 ), 4, 0 );
		assertEquals( hc.sideLength( 3 ), 4, 0 );
		assertEquals( hc.center()[ 0 ], 5, 0 );
		assertEquals( hc.center()[ 1 ], 5, 0 );
		assertEquals( hc.center()[ 2 ], 5, 0 );
		assertEquals( hc.center()[ 3 ], 5, 0 );
		assertTrue( hc.boundaryType() == BoundaryType.OPEN );
	}

	@Test
	public void testHighDimensionalClosedBox()
	{
		final Box hc = new ClosedBox( new double[] { 3, 3, 3, 3 }, new double[] { 7, 7, 7, 7 } );

		// vertices
		assertTrue( hc.test( new RealPoint( new double[] { 3, 3, 3, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 3, 3, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 3, 7, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 3, 7, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 7, 3, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 7, 3, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 7, 7, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 7, 7, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 3, 3, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 3, 3, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 3, 7, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 3, 7, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 7, 3, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 7, 3, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 7, 7, 3 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 7, 7, 7 } ) ) );

		// should contain:
		assertTrue( hc.test( new RealPoint( new double[] { 7, 3, 5, 4 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 7, 7, 4, 4 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 3, 6, 5, 5 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 6, 6, 5, 7 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 4, 4, 4, 4 } ) ) );
		assertTrue( hc.test( new RealPoint( new double[] { 5, 6, 4, 5 } ) ) );

		// should not contain:
		assertFalse( hc.test( new RealPoint( new double[] { 2, 3, 3, 3 } ) ) );
		assertFalse( hc.test( new RealPoint( new double[] { 3, 2, 3, 3 } ) ) );

		// box characteristics
		assertEquals( hc.sideLength( 0 ), 4, 0 );
		assertEquals( hc.sideLength( 1 ), 4, 0 );
		assertEquals( hc.sideLength( 2 ), 4, 0 );
		assertEquals( hc.sideLength( 3 ), 4, 0 );
		assertEquals( hc.center()[ 0 ], 5, 0 );
		assertEquals( hc.center()[ 1 ], 5, 0 );
		assertEquals( hc.center()[ 2 ], 5, 0 );
		assertEquals( hc.center()[ 3 ], 5, 0 );
		assertTrue( hc.boundaryType() == BoundaryType.CLOSED );
	}

	@Test
	public void testMutatingClosedBox()
	{
		final Box b = new ClosedBox( new double[] { 2, 2 }, new double[] { 5, 5 } );

		assertEquals( b.center()[ 0 ], 3.5, 0 );
		assertEquals( b.sideLength( 1 ), 3, 0 );
		assertTrue( b.test( new RealPoint( new double[] { 3.25, 4 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 4.5, 11.125 } ) ) );

		b.setCenter( new double[] { 5, 10 } );

		assertEquals( b.center()[ 0 ], 5, 0 );
		assertEquals( b.sideLength( 1 ), 3, 0 );
		assertFalse( b.test( new RealPoint( new double[] { 3.25, 4 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 4.5, 11.125 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 2.125, 11.25 } ) ) );

		b.setSideLength( 0, 6 );

		assertEquals( b.sideLength( 0 ), 6, 0 );
		assertEquals( b.sideLength( 1 ), 3, 0 );
		assertTrue( b.test( new RealPoint( new double[] { 2.125, 11.25 } ) ) );
	}

	@Test
	public void testMutatingOpenBox()
	{
		final Box b = new OpenBox( new double[] { 1.25, 2 }, new double[] { 5, 3.5 } );

		assertEquals( b.center()[ 0 ], 3.125, 0 );
		assertEquals( b.center()[ 1 ], 2.75, 0 );
		assertEquals( b.sideLength( 0 ), 3.75, 0 );
		assertEquals( b.sideLength( 1 ), 1.5, 0 );
		assertTrue( b.test( new RealPoint( new double[] { 3, 2.125 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 0.5, 2.75 } ) ) );

		b.setCenter( new double[] { 2, 3 } );

		assertEquals( b.center()[ 0 ], 2, 0 );
		assertEquals( b.center()[ 1 ], 3, 0 );
		assertEquals( b.sideLength( 0 ), 3.75, 0 );
		assertEquals( b.sideLength( 1 ), 1.5, 0 );
		assertFalse( b.test( new RealPoint( new double[] { 3, 2.125 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 0.5, 2.75 } ) ) );
		assertFalse( b.test( new RealPoint( new double[] { 0.25, -3 } ) ) );

		b.setSideLength( 1, 15 );

		assertEquals( b.center()[ 0 ], 2, 0 );
		assertEquals( b.center()[ 1 ], 3, 0 );
		assertEquals( b.sideLength( 0 ), 3.75, 0 );
		assertEquals( b.sideLength( 1 ), 15, 0 );
		assertTrue( b.test( new RealPoint( new double[] { 3, 2.125 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 0.5, 2.75 } ) ) );
		assertTrue( b.test( new RealPoint( new double[] { 0.25, -3 } ) ) );
	}

	@Test
	public void testMinGreaterThanMax()
	{
		exception.expect( IllegalArgumentException.class );
		new ClosedBox( new double[] { 1, 2, 3 }, new double[] { 1, 2 } );
	}

	@Test
	public void testMaxGreaterThanMin()
	{
		final Box b = new ClosedBox( new double[] { 1, 2, 3 }, new double[] { 3, 4, 5, 6 } );

		assertEquals( b.numDimensions(), 3 );
		assertEquals( b.sideLength( 2 ), 2, 0 );

		exception.expect( ArrayIndexOutOfBoundsException.class );
		b.sideLength( 3 );
	}

	@Test
	public void testSetNegativeEdgeLength()
	{
		final Box b = new OpenBox( new double[] { 1, 1 }, new double[] { 11, 11 } );

		exception.expect( IllegalArgumentException.class );
		b.setSideLength( 1, -2.25 );
	}

	@Test
	public void testSetCenterTooShort()
	{
		final Box b = new OpenBox( new double[] { 1, 1 }, new double[] { 11, 11 } );

		exception.expect( IllegalArgumentException.class );
		b.setCenter( new double[] { 3 } );
	}

	@Test
	public void testSetCenterTooLong()
	{
		final Box b = new OpenBox( new double[] { 1, 1 }, new double[] { 11, 11 } );
		b.setCenter( new double[] { 7.25, 3.125, 4 } );

		final double[] c = b.center();
		assertEquals( c.length, 2 );
		assertEquals( c[ 0 ], 7.25, 0 );
		assertEquals( c[ 1 ], 3.125, 0 );
	}

	@Test
	public void testBounds()
	{
		// Bounds should be the same for open or closed boxes
		double[] min = new double[] { 10, 6 };
		double[] max = new double[] { 12.5, 20 };
		final Box b = new ClosedBox( min, max );
		final double[] bMax = new double[ 2 ];
		final double[] bMin = new double[ 2 ];
		b.realMax( bMax );
		b.realMin( bMin );

		assertArrayEquals( max, bMax, 0 );
		assertArrayEquals( min, bMin, 0 );

		// Mutate box
		b.setSideLength( 0, 2 );
		b.realMax( bMax );
		b.realMin( bMin );
		min[ 0 ] = 10.25;
		max[ 0 ] = 12.25;
		assertArrayEquals( max, bMax, 0 );
		assertArrayEquals( min, bMin, 0 );

		b.setCenter( new double[] { 4, 4 } );
		b.realMax( bMax );
		b.realMin( bMin );
		min = new double[] { 3, -3 };
		max = new double[] { 5, 11 };
		assertArrayEquals( max, bMax, 0 );
		assertArrayEquals( min, bMin, 0 );
	}
}