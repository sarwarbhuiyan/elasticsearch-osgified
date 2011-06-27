package com.humanet.elasticsearch.internal;

import com.humanet.elasticsearch.ExampleBean;

/**
 * Internal implementation of our example Spring Bean
 */
public class ExampleBeanImpl
    implements ExampleBean
{
    public boolean isABean()
    {
        return true;
    }
}
