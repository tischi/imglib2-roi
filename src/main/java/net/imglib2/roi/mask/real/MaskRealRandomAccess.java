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
package net.imglib2.roi.mask.real;

import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccess;
import net.imglib2.roi.mask.Mask;
import net.imglib2.type.BooleanType;

/**
 * {@link RealRandomAccess} based on {@link Mask} with {@link RealLocalizable}s.
 *
 * @author Christian Dietz, University of Konstanz
 * @author Tobias Pietzsch
 */
public class MaskRealRandomAccess< B extends BooleanType< B > > extends RealPoint implements RealRandomAccess< B >
{
	private final Mask< RealLocalizable > contains;

	private final B type;

	public MaskRealRandomAccess( final Mask< RealLocalizable > contains, final B type )
	{
		super( contains.numDimensions() );
		this.contains = contains;
		this.type = type.copy();
	}

	protected MaskRealRandomAccess( final MaskRealRandomAccess< B > cra )
	{
		super( cra.numDimensions() );
		contains = cra.contains;
		type = cra.type.copy();
	}

	@Override
	public B get()
	{
		type.set( contains.test( this ) );
		return type;
	}

	@Override
	public MaskRealRandomAccess< B > copy()
	{
		return new MaskRealRandomAccess<>( this );
	}

	@Override
	public RealRandomAccess< B > copyRealRandomAccess()
	{
		return copy();
	}
}
