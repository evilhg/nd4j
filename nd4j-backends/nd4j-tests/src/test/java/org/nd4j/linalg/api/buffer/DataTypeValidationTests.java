package org.nd4j.linalg.api.buffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nd4j.linalg.BaseNd4jTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.executioner.GridExecutioner;
import org.nd4j.linalg.exception.ND4JIllegalStateException;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.factory.Nd4jBackend;

/**
 * @author raver119@gmail.com
 */
@RunWith(Parameterized.class)
public class DataTypeValidationTests extends BaseNd4jTest {
    DataBuffer.Type initialType;

    public DataTypeValidationTests(Nd4jBackend backend) {
        super(backend);
    }

    @Before
    public void setUp() {
        initialType = Nd4j.dataType();
        Nd4j.setDataType(DataBuffer.Type.FLOAT);
    }

    @After
    public void shutUp() {
        Nd4j.setDataType(initialType);
    }

    /**
     * Testing basic assign
     */
    @Test(expected = ND4JIllegalStateException.class)
    public void testOpValidation1() {
        INDArray x = Nd4j.create(10);

        Nd4j.setDataType(DataBuffer.Type.DOUBLE);

        INDArray y = Nd4j.create(10);

        x.addi(y);

        if (Nd4j.getExecutioner() instanceof GridExecutioner)
            ((GridExecutioner) Nd4j.getExecutioner()).flushQueueBlocking();
    }

    /**
     * Testing level1 blas
     */
    @Test(expected = ND4JIllegalStateException.class)
    public void testBlasValidation1() {
        INDArray x = Nd4j.create(10);

        Nd4j.setDataType(DataBuffer.Type.DOUBLE);

        INDArray y = Nd4j.create(10);

        Nd4j.getBlasWrapper().dot(x, y);
    }

    /**
     * Testing level2 blas
     */
    @Test(expected = ND4JIllegalStateException.class)
    public void testBlasValidation2() {
        INDArray a = Nd4j.create(100, 10);
        INDArray x = Nd4j.create(100);

        Nd4j.setDataType(DataBuffer.Type.DOUBLE);

        INDArray y = Nd4j.create(100);

        Nd4j.getBlasWrapper().gemv(1.0, a, x, 1.0, y);
    }

    /**
     * Testing level3 blas
     */
    @Test(expected = ND4JIllegalStateException.class)
    public void testBlasValidation3() {
        INDArray x = Nd4j.create(100, 100);

        Nd4j.setDataType(DataBuffer.Type.DOUBLE);

        INDArray y = Nd4j.create(100, 100);

        x.mmul(y);
    }


    @Override
    public char ordering() {
        return 'c';
    }
}
