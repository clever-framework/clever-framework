package com.toquery.framework.example.test.java.lambda;

/**
 * Java12 Collectors.teeing 的使用  <a href="https://segmentfault.com/a/1190000020933574">link</a>
 */
public class TestCollectorTeeing {

    /*


    // TestCollectorTeeing.CountSum(count=7, sum=46)
    @Test
    public void test() {
        CountSum countsum = Stream.of(2, 11, 1, 5, 7, 8, 12)
                .collect(Collectors.teeing(
                        Collectors.counting(),
                        Collectors.summingInt(e -> e),
                        CountSum::new));

        System.out.println(countsum.toString());
    }

    @Data
    @ToString
    class CountSum {
        private Long count;
        private Integer sum;

        public CountSum(Long count, Integer sum) {
            this.count = count;
            this.sum = sum;
        }
    }


    // TestCollectorTeeing.MinMax(min=1, max=12)
    @Test
    public void testMinMax() {
        MinMax minmax = Stream.of(2, 11, 1, 5, 7, 8, 12)
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.naturalOrder()),
                        Collectors.maxBy(Comparator.naturalOrder()),
                        (Optional<Integer> a, Optional<Integer> b) -> new MinMax(a.orElse(Integer.MIN_VALUE), b.orElse(Integer.MAX_VALUE))));
        System.out.println(minmax.toString());
    }

    // TestCollectorTeeing.MinMax(min=-2147483648, max=2147483647)
    @Test
    public void testMinMax2() {
        MinMax minmax = Stream.of(null, 2, 11, 1, 5, 7, 8, 12)
                .collect(Collectors.teeing(
                        Collectors.minBy(Comparator.nullsFirst(Comparator.naturalOrder())),
                        Collectors.maxBy(Comparator.nullsLast(Comparator.naturalOrder())),
                        (Optional<Integer> a, Optional<Integer> b) -> new MinMax(a.orElse(Integer.MIN_VALUE), b.orElse(Integer.MAX_VALUE))));
        System.out.println(minmax.toString());
    }

    @Data
    @ToString
    class MinMax {
        private Integer min;
        private Integer max;

        public MinMax(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }
    }

    // TestCollectorTeeing.WeightsAndTotal(totalWeight=19500, weights=[1200, 3000, 2600, 1600, 1200, 2600, 1700, 3000, 2600])
    @Test
    public void testMelon() {
        List<Melon> melons = Arrays.asList(new Melon("Crenshaw", 1200),
                new Melon("Gac", 3000), new Melon("Hemi", 2600),
                new Melon("Hemi", 1600), new Melon("Gac", 1200),
                new Melon("Apollo", 2600), new Melon("Horned", 1700),
                new Melon("Gac", 3000), new Melon("Hemi", 2600)
        );


        WeightsAndTotal weightsAndTotal = melons.stream()
                .collect(Collectors.teeing(
                        Collectors.summingInt(Melon::getWeight),
                        Collectors.mapping(m -> m.getWeight(), Collectors.toList()),
                        WeightsAndTotal::new));
        System.out.println(weightsAndTotal.toString());
    }


    // 定义瓜的类型和重量
    @Data
    @ToString
    class Melon {
        private String type;
        private int weight;

        public Melon(String type, int weight) {
            this.type = type;
            this.weight = weight;
        }
    }

    // 总重和单个重量列表
    @Data
    @ToString
    class WeightsAndTotal {
        private int totalWeight;
        private List<Integer> weights;

        public WeightsAndTotal(int totalWeight, List<Integer> weights) {
            this.totalWeight = totalWeight;
            this.weights = weights;
        }
    }

    // TestCollectorTeeing.EventParticipation(guestNameList=[Marco, Roger], totalNumberOfParticipants=11)

    @Test
    public void testEventParticipation() {
        EventParticipation result = Stream.of(
                new Guest("Marco", true, 3),
                new Guest("David", false, 2),
                new Guest("Roger", true, 6))
                .collect(
                        Collectors.teeing(
                                Collectors.filtering(Guest::isParticipating, Collectors.mapping(Guest::getName, Collectors.toList())),
                                Collectors.summingInt(Guest::getParticipantsNumber),
                                EventParticipation::new)
                );
        System.out.println(result);
    }

    @Data
    @ToString
    class Guest {
        private String name;
        private boolean participating;
        private Integer participantsNumber;

        public Guest(String name, boolean participating, Integer participantsNumber) {
            this.name = name;
            this.participating = participating;
            this.participantsNumber = participantsNumber;
        }
    }

    @Data
    @ToString
    class EventParticipation {
        private List<String> guestNameList;
        private Integer totalNumberOfParticipants;

        public EventParticipation(List<String> guestNameList, Integer totalNumberOfParticipants) {
            this.guestNameList = guestNameList;
            this.totalNumberOfParticipants = totalNumberOfParticipants;
        }
    }
     */
}
