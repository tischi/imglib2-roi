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
import net.imglib2.roi.geom.real.ClosedEllipsoid;
import net.imglib2.roi.geom.real.Ellipsoid;
import net.imglib2.roi.geom.real.OpenEllipsoid;
import net.imglib2.roi.mask.Mask.BoundaryType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for {@link Ellipsoid}.
 *
 * @author Alison Walter
 */
public class EllipsoidTest
{
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Test
	public void testOpenEllipsoid()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 12, 23 }, new double[] { 4, 9 } );

		// vertices
		assertFalse( e.test( new RealPoint( new double[] { 16, 23 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 8, 23 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 12, 32 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 12, 14 } ) ) );

		// inside
		assertTrue( e.test( new RealPoint( new double[] { 11, 20 } ) ) );

		// outside
		assertFalse( e.test( new RealPoint( new double[] { 15, 30 } ) ) );

		// ellipsoid characteristics
		assertEquals( e.exponent(), 2, 0 );
		assertEquals( e.center()[ 0 ], 12, 0 );
		assertEquals( e.center()[ 1 ], 23, 0 );
		assertEquals( e.semiAxisLength( 0 ), 4, 0 );
		assertEquals( e.semiAxisLength( 1 ), 9, 0 );
		assertTrue( e.boundaryType() == BoundaryType.OPEN );
	}

	@Test
	public void testClosedEllipsoid()
	{
		final Ellipsoid e = new ClosedEllipsoid( new double[] { 12, 23 }, new double[] { 4, 9 } );

		// vertices
		assertTrue( e.test( new RealPoint( new double[] { 16, 23 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 8, 23 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 12, 32 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 12, 14 } ) ) );

		// inside
		assertTrue( e.test( new RealPoint( new double[] { 11, 20 } ) ) );

		// outside
		assertFalse( e.test( new RealPoint( new double[] { 15, 30 } ) ) );

		// ellipsoid characteristics
		assertEquals( e.exponent(), 2, 0 );
		assertEquals( e.center()[ 0 ], 12, 0 );
		assertEquals( e.center()[ 1 ], 23, 0 );
		assertEquals( e.semiAxisLength( 0 ), 4, 0 );
		assertEquals( e.semiAxisLength( 1 ), 9, 0 );
		assertTrue( e.boundaryType() == BoundaryType.CLOSED );
	}

	@Test
	public void testEllipsoidSetExponent()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 1, 1 }, new double[] { 0.25, 3 } );

		exception.expect( UnsupportedOperationException.class );
		e.setExponent( 3 );
	}

	@Test
	public void testMutateOpenEllipsoid()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 0, 6.25 }, new double[] { 1, 2.25 } );

		assertEquals( e.center()[ 0 ], 0, 0 );
		assertEquals( e.center()[ 1 ], 6.25, 0 );
		assertEquals( e.semiAxisLength( 0 ), 1, 0 );
		assertEquals( e.semiAxisLength( 1 ), 2.25, 0 );
		assertTrue( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );

		// change center
		e.setCenter( new double[] { 10, 15 } );

		assertEquals( e.center()[ 0 ], 10, 0 );
		assertEquals( e.center()[ 1 ], 15, 0 );
		assertFalse( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );

		// change semi-axis length
		e.setSemiAxisLength( 0, 4 );

		assertEquals( e.semiAxisLength( 0 ), 4, 0 );
		assertEquals( e.semiAxisLength( 1 ), 2.25, 0 );
		assertFalse( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );
	}

	@Test
	public void testMutateClosedEllipsoid()
	{
		final Ellipsoid e = new ClosedEllipsoid( new double[] { 0, 6.25 }, new double[] { 1, 2.25 } );

		assertEquals( e.center()[ 0 ], 0, 0 );
		assertEquals( e.center()[ 1 ], 6.25, 0 );
		assertEquals( e.semiAxisLength( 0 ), 1, 0 );
		assertEquals( e.semiAxisLength( 1 ), 2.25, 0 );
		assertTrue( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );

		// change center
		e.setCenter( new double[] { 10, 15 } );

		assertEquals( e.center()[ 0 ], 10, 0 );
		assertEquals( e.center()[ 1 ], 15, 0 );
		assertFalse( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertFalse( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );

		// change semi-axis length
		e.setSemiAxisLength( 0, 4 );

		assertEquals( e.semiAxisLength( 0 ), 4, 0 );
		assertEquals( e.semiAxisLength( 1 ), 2.25, 0 );
		assertFalse( e.test( new RealPoint( new double[] { 0.5, 7 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 9.25, 16 } ) ) );
		assertTrue( e.test( new RealPoint( new double[] { 13.5, 16 } ) ) );
	}

	@Test
	public void testNegativeSemiAxisLength()
	{
		exception.expect( IllegalArgumentException.class );
		new ClosedEllipsoid( new double[] { 0, 0 }, new double[] { 3, -6 } );
	}

	@Test
	public void testZeroSemiAxisLength()
	{
		exception.expect( IllegalArgumentException.class );
		new OpenEllipsoid( new double[] { 0, 0 }, new double[] { 0, 4 } );
	}

	@Test
	public void testCenterLonger()
	{
		final Ellipsoid e = new ClosedEllipsoid( new double[] { 0, 0, 0, 0 }, new double[] { 3, 7 } );

		assertEquals( e.numDimensions(), 2 );
		assertEquals( e.center().length, 2 );
		assertArrayEquals( e.center(), new double[] { 0, 0 }, 0 );
	}

	@Test
	public void testSemiAxisLengthLonger()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 0, 0 }, new double[] { 3, 6, 9 } );

		assertEquals( e.numDimensions(), 2 );
		assertArrayEquals( e.center(), new double[] { 0, 0 }, 0 );

		assertEquals( e.semiAxisLength( 0 ), 3, 0 );
		assertEquals( e.semiAxisLength( 1 ), 6, 0 );

		exception.expect( ArrayIndexOutOfBoundsException.class );
		e.semiAxisLength( 2 );
	}

	@Test
	public void testSetNegativeSemiAxisLength()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 0, 0 }, new double[] { 3, 6 } );

		exception.expect( IllegalArgumentException.class );
		e.setSemiAxisLength( 0, -0.125 );
	}

	@Test
	public void testSetCenterTooShort()
	{
		final Ellipsoid e = new OpenEllipsoid( new double[] { 0, 0, 0 }, new double[] { 3, 6, 9 } );

		exception.expect( IllegalArgumentException.class );
		e.setCenter( new double[] { 4, 3 } );
	}

	@Test
	public void testSetCenterTooLong()
	{
		final Ellipsoid e = new ClosedEllipsoid( new double[] { 0, 0 }, new double[] { 3, 7 } );

		e.setCenter( new double[] { 4, 12, 5 } );
		final double[] c = e.center();
		assertEquals( c.length, 2 );
		assertEquals( c[ 0 ], 4, 0 );
		assertEquals( c[ 1 ], 12, 0 );
	}

	@Test
	public void testBounds()
	{
		// Bounds should be the same for open or closed ellipsoids
		final Ellipsoid e = new OpenEllipsoid( new double[] { 12, 23 }, new double[] { 4, 9 } );
		double[] min = new double[] { 12 - 4, 23 - 9 };
		double[] max = new double[] { 12 + 4, 23 + 9 };
		final double[] eMin = new double[ 2 ];
		final double[] eMax = new double[ 2 ];
		e.realMin( eMin );
		e.realMax( eMax );

		assertArrayEquals( min, eMin, 0 );
		assertArrayEquals( max, eMax, 0 );

		// Mutate ellipsoids
		e.setSemiAxisLength( 0, 0.25 );
		min[ 0 ] = 12 - 0.25;
		max[ 0 ] = 12 + 0.25;
		e.realMin( eMin );
		e.realMax( eMax );
		assertArrayEquals( min, eMin, 0 );
		assertArrayEquals( max, eMax, 0 );

		e.setCenter( new double[] { 120, -30 } );
		min = new double[] { 120 - 0.25, -30 - 9 };
		max = new double[] { 120 + 0.25, -30 + 9 };
		e.realMin( eMin );
		e.realMax( eMax );
		assertArrayEquals( min, eMin, 0 );
		assertArrayEquals( max, eMax, 0 );
	}
}
