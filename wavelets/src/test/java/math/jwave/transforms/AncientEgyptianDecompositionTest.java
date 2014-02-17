/**
 * JWave - Java implementation of wavelet transform algorithms
 *
 * Copyright 2010-2014 Christian Scheiblich
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * This file AncientEgyptianDecompositionTest.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.11.2010 19:41:42
 * contact cscheiblich@gmail.com
 */
package math.jwave.transforms;

import math.jwave.JUnitTests;
import math.jwave.Transform;
import math.jwave.exc.JWaveException;
import math.jwave.transforms.AncientEgyptianDecomposition;
import math.jwave.transforms.FastWaveletTransform;
import math.jwave.transforms.WaveletPacketTransform;
import math.jwave.transforms.wavelets.Haar02;
import org.junit.Test;

public class AncientEgyptianDecompositionTest extends JUnitTests {
  
  /**
   * Test method for {@link math.jwave.Transform#forward(double[])}.
   * @throws JWaveException 
   */
  @Test
  public void testAncientEgyptianDecompositionFwtForwardHaar02Array( ) throws JWaveException {
    
    System.out.println( "" );
    System.out.println( "testAncientEgyptianDecompositionFwtForwardHaar02Array" );
    
    double delta = 1.e-12;
    
    double[ ] arrTime = {
    1., 1., 1., 1., 1., 1., 1.
    }; // length 7 = 2^2 + 2^1 + 2^0
    
    showTime( arrTime );
    
    Transform t = new Transform( new AncientEgyptianDecomposition( new FastWaveletTransform( new Haar02( ) ) ) );
    double[ ] arrHilb = t.forward( arrTime );
    
    showHilb( arrHilb );
    
    double[ ] expected = {
    2., 0., 0., 0., Math.sqrt( 2. ), 0., 1.
    };
    assertArray( expected, arrHilb, delta );
    
  }
  
  /**
   * Test method for {@link math.jwave.Transform#reverse(double[])}.
   * @throws JWaveException 
   */
  @Test
  public void testAncientEgyptianDecompositionFwtReverseHaar02Array( ) throws JWaveException {
    
    System.out.println( "" );
    System.out.println( "testAncientEgyptianDecompositionFwtReverseHaar02Array" );
    
    double delta = 1e-12;
    
    double[ ] arrHilb = {
    2., 0., 0., 0., Math.sqrt( 2. ), 0., 1.
    }; // length 7 = 2^2 + 2^1 + 2^0
    
    showHilb( arrHilb );
    
    Transform t = new Transform( new AncientEgyptianDecomposition( new FastWaveletTransform( new Haar02( ) ) ) );
    double[ ] arrTime = t.reverse( arrHilb );
    
    showTime( arrTime );
    
    double[ ] expected = {
    1., 1., 1., 1., 1., 1., 1.
    };
    assertArray( expected, arrTime, delta );
    
  }
  
  /**
   * Test method for {@link math.jwave.Transform#forward(double[])}.
   * @throws JWaveException 
   */
  @Test
  public void testAncientEgyptianDecompositionWptForwardHaar02Array( ) throws JWaveException {
    
    System.out.println( "" );
    System.out.println( "testAncientEgyptianDecompositionWptForwardHaar02Array" );
    
    double delta = 1.e-12;
    
    double[ ] arrTime = {
    1., 2., 3., 4., 2., 0., 1.
    }; // length 7 = 2^2 + 2^1 + 2^0
    
    showTime( arrTime );
    
    Transform t = new Transform( new AncientEgyptianDecomposition( new WaveletPacketTransform( new Haar02( ) ) ) );
    double[ ] arrHilb = t.forward( arrTime );
    
    showHilb( arrHilb );
    
    double sqrt2 = Math.sqrt( 2. );
    double[ ] expected = {
    5., -2., -1., 0., sqrt2, sqrt2, 1.
    };
    assertArray( expected, arrHilb, delta );
    
  }
  
  /**
   * Test method for {@link math.jwave.Transform#reverse(double[])}.
   * @throws JWaveException 
   */
  @Test
  public void testAncientEgyptianDecompositionWptReverseHaar02Array( ) throws JWaveException {
    
    System.out.println( "" );
    System.out.println( "testAncientEgyptianDecompositionWptReverseHaar02Array" );
    
    double delta = 1e-12;
    
    double sqrt2 = Math.sqrt( 2. );
    double[ ] arrHilb = {
    5., -2., -1., 0., sqrt2, sqrt2, 1.
    };
    
    showHilb( arrHilb );
    
    Transform t = new Transform( new AncientEgyptianDecomposition( new WaveletPacketTransform( new Haar02( ) ) ) );
    double[ ] arrTime = t.reverse( arrHilb );
    
    showTime( arrTime );
    
    double[ ] expected = {
    1., 2., 3., 4., 2., 0., 1.
    };
    assertArray( expected, arrTime, delta );
    
  }
  
}
