package com.jjeanniard.mods;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MyConfig {

        public String firstJoin;
        public String rejoin;

        
        public static final BuilderCodec<MyConfig> CODEC = BuilderCodec.builder(MyConfig.class, MyConfig::new)
                        .append(new KeyedCodec<>("Welcome.firstJoin", Codec.STRING),
                                        (config, value, info) -> config.firstJoin = value,
                                        (config, info) -> config.firstJoin)
                        .add()
                        .append(new KeyedCodec<>("Welcome.rejoin", Codec.STRING),
                                        (config, value, info) -> config.rejoin = value,
                                        (config, info) -> config.rejoin)
                        .add()

                        .build();

        public MyConfig() {
        }

        public String getFirstJoinMessage() {
                return firstJoin;
        }

        public String getRejoinMessage() {
                return rejoin;
        }
}
